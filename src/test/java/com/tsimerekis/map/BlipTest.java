package com.tsimerekis.map;

import com.tsimerekis.Helper;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.map.configuration.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlipTest {

    @Test
    void createBlipXY() {
        final Blip blip = Blip.createBlip(1.5,-1.5);
        assertNotNull(blip);
    }

    @Test
    void createBlipSubmission() {
        final Blip blip = createSampleBlipFromSubmission();

        assertNotNull(blip);
    }

    @Test
    void submissionBlipCoordinateCheckNotNull() {
        final Blip blip = createSampleBlipFromSubmission();

        final Coordinate coordinate = blip.getCoordinates();

        assertNotNull(coordinate);
    }

    @Test
    void submissionBlipCoordinateCheckSameCoordinate() {
        final Blip blip = createSampleBlipFromSubmission();

        final Coordinate coordinate = blip.getCoordinates();

        assertEquals(1.5, coordinate.getX());
        assertEquals(-1.5, coordinate.getY());
    }

    private Blip createSampleBlipFromSubmission() {
        final Submission submission = new Submission();
        submission.setLocation(Helper.point(1.5,-1.5));

        return Blip.createBlip(submission);
    }
}