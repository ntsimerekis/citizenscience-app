package com.tsimerekis.map.proxy;

import com.tsimerekis.submission.service.SubmissionService;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@UIScope
public class SubmissionProxy {

    private final Integer PROXY_SIZE = 100;

    private final SubmissionService submissionService;

    private final Map<Long, Submission> submissions = new HashMap<>();

    public SubmissionProxy(@Autowired SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    public Optional<Submission> getSubmission(long id) {
        final Submission submission;
        if ( (submission = submissions.get(id)) != null) {
            return Optional.of(submission);
        } else {
            final Optional<Submission> retrievedSubmission = submissionService.findById(id);
            retrievedSubmission.ifPresent(s -> submissions.put(id, s));

            return retrievedSubmission;
        }
    }

}
