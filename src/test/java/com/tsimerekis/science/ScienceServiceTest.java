package com.tsimerekis.science;

import com.tsimerekis.submission.entity.Submission;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScienceServiceTest {

    @Autowired
    private ScienceService scienceService;

    @Test
    void averagingReportNotNull() {
        List<Submission> emptySubmission = new ArrayList<>();

        AveragingReport report = scienceService.getAveragingReport(emptySubmission);

        assertNotNull(report);
    }

    @Test
    void emptyReport() {
        List<Submission> emptySubmission = new ArrayList<>();

        AveragingReport report = scienceService.getAveragingReport(emptySubmission);

        assertEquals(0, report.getAveragePM10());
        assertEquals(0, report.getAveragePM25());
        assertEquals(0, report.getAverageAltitude());
        assertEquals(0, report.getAverageTemperature());
        assertFalse(report.speciesSubmissionPresent());
        assertFalse(report.pollutionReportPresent());
    }
}