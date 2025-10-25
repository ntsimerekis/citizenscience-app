package com.tsimerekis.science;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AveragingReportTest {

    private AveragingReport sampleAveragingReport() {
        return new AveragingReport();
    }

    @Test
    void speciesSubmissionPresent() {
        Submission speciesSpotting = new SpeciesSpotting();

        AveragingReport averagingReport = sampleAveragingReport();
        averagingReport.addReport(speciesSpotting);

        assertTrue(averagingReport.speciesSubmissionPresent());
    }

    @Test
    void pollutionReportPresent() {
        Submission pollutionReport = new PollutionReport();

        AveragingReport sampleAveragingReport = sampleAveragingReport();
        sampleAveragingReport.addReport(pollutionReport);

        assertTrue(sampleAveragingReport.pollutionReportPresent());
    }

    @Test
    void getAveragePM10() {
    }

    @Test
    void setAveragePM10() {
    }

    @Test
    void getAveragePM25() {
    }

    @Test
    void setAveragePM25() {
    }

    @Test
    void getAverageTemperature() {
    }

    @Test
    void setAverageTemperature() {
    }

    @Test
    void getAverageAltitude() {
    }

    @Test
    void setAverageAltitude() {
    }
}