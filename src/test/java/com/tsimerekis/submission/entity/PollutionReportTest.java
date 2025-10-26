package com.tsimerekis.submission.entity;

import com.tsimerekis.Helper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PollutionReportTest {

    private static final Float PM10 = 3.0f;
    private static final Float PM25 = 4.0f;
    private static final Float HOURS = 1.0f;

    private PollutionReport minimumReport() {
        final PollutionReport report = new PollutionReport();
        report.setLocation(Helper.samplePoint());
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