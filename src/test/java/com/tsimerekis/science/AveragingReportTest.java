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
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        pollutionReport1.setPM10(5.0f);
        PollutionReport pollutionReport2 = new PollutionReport();
        pollutionReport2.setPM10(5.0f);

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(5.0f, averagingReport.getAveragePM10());
    }

    @Test
    void getAveragePM10FromNull() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        PollutionReport pollutionReport2 = new PollutionReport();

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(0.0f, averagingReport.getAveragePM10());
    }

    @Test
    void getAveragePM25() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        pollutionReport1.setPM25(5.0f);
        PollutionReport pollutionReport2 = new PollutionReport();
        pollutionReport2.setPM25(5.0f);

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(5.0f, averagingReport.getAveragePM25());
    }

    @Test
    void getAveragePM25FromNull() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        PollutionReport pollutionReport2 = new PollutionReport();

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(0.0f, averagingReport.getAveragePM25());
    }

    @Test
    void getAverageTemperature() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        pollutionReport1.setTemperatureCelsius(5.0f);
        PollutionReport pollutionReport2 = new PollutionReport();
        pollutionReport2.setTemperatureCelsius(5.0f);

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(5.0f, averagingReport.getAverageTemperature());
    }

    @Test
    void getAverageTemperatureFromNull() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        PollutionReport pollutionReport2 = new PollutionReport();

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(0.0f, averagingReport.getAverageTemperature());
    }

    @Test
    void getAverageAltitude() {
        AveragingReport averagingReport = sampleAveragingReport();
        PollutionReport pollutionReport1 = new PollutionReport();
        pollutionReport1.setAltitudeMeters(5.0f);
        PollutionReport pollutionReport2 = new PollutionReport();
        pollutionReport2.setAltitudeMeters(5.0f);

        averagingReport.addReport(pollutionReport1);
        averagingReport.addReport(pollutionReport2);

        assertEquals(5.0f, averagingReport.getAverageAltitude());
    }

}