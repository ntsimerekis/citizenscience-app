package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.entity.SubmissionRepository;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.exception.MissingSpeciesException;
import com.tsimerekis.submission.species.Species;
import com.tsimerekis.submission.species.SpeciesRepository;
import jakarta.persistence.criteria.Predicate;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    public List<Submission> findWithinGeometry(Geometry geometry) {
        return submissionRepository.finAllInBbox(geometry);
    }

    public Optional<Submission> findById(Long id) {
        if (id == null) return Optional.empty();

        return submissionRepository.findById(id);
    }

    public List<Submission> findAllByCriteria(FilterCriteria criteria) {
        return submissionRepository.findAll(byCriteria(criteria));
    }

    public void save(Submission submission) {
        if (submission.isValid()) {
            submissionRepository.save(submission);
        } else {
            throw new InvalidSubmissionException();
        }
    }

    public void save(SpeciesSpotting submission) {
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

            submissionRepository.save(submission);
        } else {
            if (submission.getSpecies() == null) {
                throw new MissingSpeciesException();
            }

            throw new InvalidSubmissionException();
        }
    }

    public List<Submission> getAll() {
        return submissionRepository.findAll();
    }

    private static Specification<Submission> byCriteria(FilterCriteria c) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (c.minTemperature != null)
                predicates.add(cb.ge(root.get("temperatureCelsius"), c.minTemperature));
            if (c.maxTemperature != null)
                predicates.add(cb.le(root.get("temperatureCelsius"), c.maxTemperature));
            if (c.minTemperature != null)
                predicates.add(cb.ge(root.get("temperatureCelsius"), c.minTemperature));
            if (c.maxTemperature != null)
                predicates.add(cb.le(root.get("temperatureCelsius"), c.maxTemperature));
            if (c.minPM25 != null)
                predicates.add(cb.ge(root.get("pm25"), c.minPM25));
            if (c.maxPM25 != null)
                predicates.add(cb.le(root.get("pm25"), c.maxPM25));
            if (c.minPM10 != null)
                predicates.add(cb.ge(root.get("pm10"), c.minPM10));
            if (c.maxPM10 != null)
                predicates.add(cb.le(root.get("pm10"), c.maxPM10));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
