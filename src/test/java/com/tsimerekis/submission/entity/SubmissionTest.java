package com.tsimerekis.submission.entity;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
public class SubmissionTest {

    @Test
    void temperatureCelsius() {
        final Submission submission = new Submission();
        submission.setTemperatureCelsius(3.0f);

        assertEquals(3.0f, submission.getTemperatureCelsius());
    }

    @Test
    void altitudeMeters() {
        final Submission submission = new Submission();
        submission.setAltitudeMeters(3.0f);

        assertEquals(3.0f, submission.getAltitudeMeters());
    }
}
