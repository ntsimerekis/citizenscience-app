package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.entity.SubmissionRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public List<Submission> findAllByCriteria(FilterCriteria criteria) {
        return submissionRepository.findAll(byCriteria(criteria));
    }

    private static Specification<Submission> byCriteria(FilterCriteria c) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (c.minTemperature != null)
                predicates.add(cb.ge(root.get("temperature"), c.minTemperature));
            if (c.maxTemperature != null)
                predicates.add(cb.le(root.get("temperature"), c.maxTemperature));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
