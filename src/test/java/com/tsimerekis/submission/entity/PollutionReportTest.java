package com.tsimerekis.submission.entity;

import com.tsimerekis.TestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PollutionReportTest {

    private static final Double PM10 = 3.0;
    private static final Double PM25 = 4.0;
    private static final Double HOURS = 1.0;

    private PollutionReport minimumReport() {
        final PollutionReport report = new PollutionReport();
        report.setLocation(TestHelper.samplePoint());
        report.setPM10(PM10);
        report.setPM25(PM25);
        report.setDurationHours(HOURS);

        return report;
    }

    @Test
    void isValid() {
        final PollutionReport report = minimumReport();

        assertTrue(report.isValid());
    }

    @Test
    void PM25() {
        final PollutionReport report = minimumReport();
        assertEquals(PM25, report.getPM25());
    }

    @Test
    void setPM25() {
        final PollutionReport report = minimumReport();
        report.setPM25(PM25 + 2);

        assertEquals(PM25 + 2, report.getPM25());
    }

    @Test
    void getPM10() {
        final PollutionReport report = minimumReport();

        assertEquals(PM10, report.getPM10());
    }

    @Test
    void setPM10() {
        final PollutionReport report = minimumReport();
        report.setPM10(PM10 + 2);

        assertEquals(PM10 + 2, report.getPM10());
    }

    @Test
    void getDurationHours() {
        final PollutionReport report = minimumReport();

        assertEquals(HOURS, report.getDurationHours());
    }

    @Test
    void setDurationHours() {
        final PollutionReport report = minimumReport();
        report.setDurationHours(HOURS + 2);

        assertEquals(HOURS + 2, report.getDurationHours());
    }

    @Test
    void sensorType() {
        final PollutionReport report = minimumReport();
        report.setSensorType("generic_sensor");

        assertEquals("generic_sensor", report.getSensorType());
    }
}