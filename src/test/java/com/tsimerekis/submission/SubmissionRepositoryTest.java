package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.entity.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.tsimerekis.TestHelper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubmissionRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepository;

    @Test
    public void pollution_report_submission() {
        PollutionReport report = new PollutionReport();
        report.setPM10(5.0);
        report.setPM25(10.0);
        report.setObservedAt(LocalDateTime.now());
        report.setLocation(TestHelper.point(-122.4194, 37.7749));

        PollutionReport saved = submissionRepository.save(report);

        assertNotNull(saved);
        assertTrue(submissionRepository.existsById(saved.getSubmissionId().get()));
    }

    @Test
    void saveSpeciesReport() {
        SpeciesSpotting submission = new SpeciesSpotting();

        Submission saved = submissionRepository.save(submission);
        assertNotNull(saved);
    }

    @Test
    void savePollutionReport() {
        PollutionReport report = new PollutionReport();

        PollutionReport saved = submissionRepository.save(report);
        assertNotNull(saved);
    }

    @Test
    void getSpeciesReportById() {
        assertTrue(submissionRepository.existsById(2L));
    }

    @Test
    void getPollutionReportById() {
        assertTrue(submissionRepository.existsById(1L));
    }

    @Test
    void testBoxRangeQuerySpatial() {
        PollutionReport reportInsideBox = new PollutionReport();
        PollutionReport reportOutsideBox = new PollutionReport();
        buildInsideBoxReport(reportInsideBox);
        buildOutsideBoxReport(reportOutsideBox);

        final List<Submission> results = conductFindAllInRangeAndBboxQuery(reportInsideBox, reportOutsideBox);

        assertEquals(1, results.size());
        assertEquals(reportInsideBox.getSubmissionId(), results.getFirst().getSubmissionId());
    }

    @Test
    void testBoxRangeQuery() {


    }

    private List<Submission> conductFindAllInRangeAndBboxQuery(PollutionReport reportInsideBox, PollutionReport reportOutsideBox) {
        submissionRepository.save(reportInsideBox);
        submissionRepository.save(reportOutsideBox);
        Instant start = LocalDateTime.of(2024, 10, 1, 0, 0).toInstant(ZoneOffset.UTC);
        Instant end = LocalDateTime.of(2024, 10, 31, 23, 59).toInstant(ZoneOffset.UTC);
        Geometry bbox = makeEnvelope(-123.0, 37.0, -121.0, 38.0); // Bay Area
        return submissionRepository.findAllInRangeAndBbox(start, end, bbox);
    }

    private void buildInsideBoxReport(PollutionReport reportInsideBox) {
        reportInsideBox.setObservedAt(LocalDateTime.of(2024, 10, 10, 12, 0));
        reportInsideBox.setLocation(TestHelper.point(-122.42, 37.77)); // San Francisco
        reportInsideBox.setPM25(12.5);
        reportInsideBox.setPM10(25.0);
    }

    private void buildOutsideBoxReport(PollutionReport reportOutsideBox) {
        reportOutsideBox.setObservedAt(LocalDateTime.of(2024, 10, 10, 13, 0));
        reportOutsideBox.setLocation(TestHelper.point(0, 0)); // Africa
        reportOutsideBox.setPM25(5.0);
        reportOutsideBox.setPM10(10.0);
    }

    @Test
    void findAllInRangeAndBbox() {
        // Define filter criteria
        Instant start = LocalDateTime.of(2024, 10, 1, 0, 0).toInstant(ZoneOffset.UTC);
        Instant end = LocalDateTime.of(2024, 10, 31, 23, 59).toInstant(ZoneOffset.UTC);
        Geometry bbox = makeEnvelope(-123.0, 37.0, -121.0, 38.0); // Bay Area

        // Run the query
        List<Submission> results = submissionRepository.findAllInRangeAndBbox(start, end, bbox);

        assertEquals(1, results.size());
        assertEquals(2, results.getFirst().getSubmissionId().get());
    }

    private Polygon makeEnvelope(double minLon, double minLat, double maxLon, double maxLat) {
        Coordinate[] coords = new Coordinate[]{
                new Coordinate(minLon, minLat),
                new Coordinate(minLon, maxLat),
                new Coordinate(maxLon, maxLat),
                new Coordinate(maxLon, minLat),
                new Coordinate(minLon, minLat)
        };
        return TestHelper.geometryFactory.createPolygon(coords);
    }

}