package com.tsimerekis.submission.service;

import com.tsimerekis.submission.repository.FilterCriteria;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.repository.SubmissionRepository;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.exception.MissingSpeciesException;
import com.tsimerekis.submission.entity.Species;
import com.tsimerekis.submission.repository.SpeciesRepository;
import com.tsimerekis.submission.repository.SubmissionSpecs;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    private final SpeciesRepository speciesRepository;

    public SubmissionService(@Autowired SubmissionRepository submissionRepository, @Autowired SpeciesRepository speciesRepository) {
        this.submissionRepository = submissionRepository;
        this.speciesRepository = speciesRepository;
    }

    public List<Submission> findWithinGeometry(Geometry geometry) {
        return submissionRepository.findAllInBbox(geometry);
    }

    public Optional<Submission> findById(Long id) {
        if (id == null) return Optional.empty();

        return submissionRepository.findById(id);
    }

    public List<Submission> findAllByCriteria(FilterCriteria criteria) {
        return submissionRepository.findAll(SubmissionSpecs.byFilterCriteria(criteria));
    }

    public List<Submission> findAllByCriteriaAndWithinGeometry(FilterCriteria criteria, Geometry geometry) {
        final Specification<Submission> specification =
            Specification.<Submission>unrestricted()
                .and(SubmissionSpecs.byFilterCriteria(criteria))
                .and(SubmissionSpecs.forPollutionReport(criteria))
                .and(SubmissionSpecs.intersects(geometry));

        var submissions = submissionRepository.findAll(specification);
        return submissions;
    }

    public void save(Submission submission) {
        if (submission.isValid()) {
            submissionRepository.save(submission);
        } else {
            throw new InvalidSubmissionException();
        }
    }

    public Submission save(SpeciesSpotting submission) {
        final Submission submissionSaved;
        if (submission.isValid()) {
            final Species species = submission.getSpecies();
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

            Example<Species> example = Example.of(species, matcher);
            List<Species> persistedSpecies = speciesRepository.findAll(example);
            if (!persistedSpecies.isEmpty()) {
                submission.setSpecies(persistedSpecies.getFirst());
            } else {
                submission.setSpecies(speciesRepository.save(species));
            }

            submissionSaved = submissionRepository.save(submission);
        } else {
            if (submission.getSpecies() == null) {
                throw new MissingSpeciesException();
            }

            throw new InvalidSubmissionException();
        }

        return submissionSaved;
    }

    public List<Submission> getAll() {
        return submissionRepository.findAll();
    }

    public List<String> ngramSearchSpecies(String species) {
        return speciesRepository.fuzzySearchSpeciesName(species)
                .stream()
                .map(Species::getSpeciesName)
                .toList();
    }

}
