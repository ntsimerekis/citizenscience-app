package com.tsimerekis.science;

import com.tsimerekis.submission.entity.Submission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScienceService {

    public AveragingReport getAveragingReport(final List<Submission> submissionList) {
        return new AveragingReport();
    }
}
