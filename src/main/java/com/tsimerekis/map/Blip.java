package com.tsimerekis.map;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.map.configuration.style.Icon;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

public class Blip extends MarkerFeature {

    private static final Icon speciesIcon = MarkerFeature.PIN_ICON;
    private static final Icon pollutionIcon = MarkerFeature.PIN_ICON;

    private Blip(Coordinate coordinate, Submission submission) {
        super(coordinate);

        if (submission != null) {
            setId(submission.getId().toString());

            if (submission instanceof SpeciesSpotting) {
                setIcon(speciesIcon);
            } else if (submission instanceof PollutionReport) {
                setIcon(pollutionIcon);
            }
        }
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
}
