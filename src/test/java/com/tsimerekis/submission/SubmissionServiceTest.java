package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.Submission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionServiceTest {

    @Autowired
    private SubmissionService submissionService;

    @Test
    void findAllByEmptyCriteria() {
        FilterCriteria criteria = new FilterCriteria();
        List<Submission> results = submissionService.findAllByCriteria(criteria);

        assertEquals(0, results.size());
    }
}