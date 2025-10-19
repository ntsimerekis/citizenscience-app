package com.tsimerekis.map;

import com.tsimerekis.submission.PollutionReport;
import com.tsimerekis.submission.SpeciesSpotting;
import com.tsimerekis.submission.Submission;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.map.configuration.style.Icon;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

public class Blip extends MarkerFeature {

    private static final Icon speciesIcon = new Icon(null);
    private static final Icon pollutionIcon = new Icon(null);

    private final Optional<Submission> submission;

    private Blip(Coordinate coordinate, Submission submission) {
        super(coordinate);
        this.submission = Optional.ofNullable(submission);

        this.submission.ifPresent(sub -> {
            setId(sub.getId().toString());

            if (sub instanceof SpeciesSpotting) {
                setIcon(speciesIcon);
            } else if (sub instanceof PollutionReport) {
                setIcon(pollutionIcon);
            }
        });
    }

    public static Blip createBlip(double x, double y) {
        return new Blip(new Coordinate(x, y), null);
    }

    public static Blip createBlip(@NotNull final Submission submission) {
        if (submission.getId() == null || submission.getLocation() == null) {
            throw new IllegalArgumentException("Blip id and location are required");
        }

        final Point jtsPoint = submission.getLocation();
        final Coordinate coordinate = new Coordinate(jtsPoint.getX(), jtsPoint.getY());

        return new Blip(coordinate, submission);
    }

    void showSubWindow() {
        submission.ifPresent(sub -> {

        });
    }
}
