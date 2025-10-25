package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
public class SubmissionTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
//  Depending on whether you'd like to persist
//  @Rollback(false)
    public void pollution_report_submission() {
        final GeometryFactory geometryFactory = new GeometryFactory();

        PollutionReport report = new PollutionReport();
        report.setPm10(5.0F);
        report.setPm25(10.0F);
        report.setObservedAt(LocalDateTime.now());
        final Point point = geometryFactory.createPoint(new Coordinate(-122.4194, 37.7749));
        report.setLocation(point);

        PollutionReport saved = submissionRepository.save(report);

        assertNotNull(saved);
        assertTrue(submissionRepository.existsById(saved.getSubmissionId().get()));

        submissionRepository.findAll().forEach(System.out::println);
    }
}
