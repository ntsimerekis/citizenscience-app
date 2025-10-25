package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.entity.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.test.context.SpringBootTest;
import com.tsimerekis.Helper;

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
    void saveSpeciesReport() {
        Submission submission = new Submission();

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
        Submission submission = new Submission();

    }

    @Test
    void getPollutionReportById() {

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
        reportInsideBox.setLocation(Helper.point(-122.42, 37.77)); // San Francisco
        reportInsideBox.setPm25(12.5f);
        reportInsideBox.setPm10(25.0f);
    }

    private void buildOutsideBoxReport(PollutionReport reportOutsideBox) {
        reportOutsideBox.setObservedAt(LocalDateTime.of(2024, 10, 10, 13, 0));
        reportOutsideBox.setLocation(Helper.point(0, 0)); // Africa
        reportOutsideBox.setPm25(5.0f);
        reportOutsideBox.setPm10(10.0f);
    }

    @Test
    void findAllInRangeAndBbox() {
        // Insert within range
        PollutionReport inRange = new PollutionReport();
        inRange.setObservedAt(LocalDateTime.of(2024, 10, 10, 12, 0));
        inRange.setLocation(Helper.point(-122.42, 37.77)); // San Francisco
        inRange.setPm25(12.5f);
        inRange.setPm10(25.0f);
        submissionRepository.save(inRange);

        // Insert outside bbox
        PollutionReport outOfBox = new PollutionReport();
        outOfBox.setObservedAt(LocalDateTime.of(2024, 10, 10, 13, 0));
        outOfBox.setLocation(Helper.point(0, 0)); // Africa
        outOfBox.setPm25(5.0f);
        outOfBox.setPm10(10.0f);
        submissionRepository.save(outOfBox);

        // Insert outside date range
        PollutionReport outOfDate = new PollutionReport();
        outOfDate.setObservedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        outOfDate.setLocation(Helper.point(-122.42, 37.77));
        outOfDate.setPm25(5.0f);
        outOfDate.setPm10(10.0f);
        submissionRepository.save(outOfDate);

        // Define filter criteria
        Instant start = LocalDateTime.of(2024, 10, 1, 0, 0).toInstant(ZoneOffset.UTC);
        Instant end = LocalDateTime.of(2024, 10, 31, 23, 59).toInstant(ZoneOffset.UTC);
        Geometry bbox = makeEnvelope(-123.0, 37.0, -121.0, 38.0); // Bay Area

        // Run the query
        List<Submission> results = submissionRepository.findAllInRangeAndBbox(start, end, bbox);

        assertEquals(1, results.size());
        assertEquals(inRange.getSubmissionId(), results.get(0).getSubmissionId());
    }

    private Polygon makeEnvelope(double minLon, double minLat, double maxLon, double maxLat) {
        Coordinate[] coords = new Coordinate[]{
                new Coordinate(minLon, minLat),
                new Coordinate(minLon, maxLat),
                new Coordinate(maxLon, maxLat),
                new Coordinate(maxLon, minLat),
                new Coordinate(minLon, minLat)
        };
        return Helper.geometryFactory.createPolygon(coords);
    }

}