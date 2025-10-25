package com.tsimerekis.submission;

import com.tsimerekis.Helper;
import com.tsimerekis.submission.entity.PollutionReport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PollutionReportTest {

    private PollutionReport minimumReport() {
        final PollutionReport report = new PollutionReport();
        report.setLocation(Helper.samplePoint());
        report.setPm10(3.0f);
        report.setPm25(4.0f);
        report.setDuration(5.0f);

        return report;
    }

    @Test
    void isValid() {
        final PollutionReport report = minimumReport();

        assertTrue(report.isValid());
    }

    @Test
    void averagePm() {
        final PollutionReport report = minimumReport();
        report.setPm10(3.0f);

        assertEquals(1.5f, report.averagePm());
    }
}