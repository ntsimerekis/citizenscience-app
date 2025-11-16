package com.tsimerekis.map.observers;

import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.submission.service.SubmissionService;
import com.vaadin.flow.component.map.events.MapClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
@UIScope
public class TestClickRadiusObserver {

    private final SubmissionService submissionService;

    public TestClickRadiusObserver(@Autowired SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @EventListener
    public void clickRadius(MapClickEvent event) {
        final var vaadinCoord = event.getCoordinate();
        final Geometry squareRadius = roughMilesSquare(vaadinCoord.getY(), vaadinCoord.getX(), 50);
        final int submissionsFound = submissionService.findWithinGeometry(squareRadius).size();

        Notification.show(Integer.toString(submissionsFound));
    }

    public static Polygon roughMilesSquare(double lat, double lon, double miles) {
        double latRad = Math.toRadians(lat);
        double milesPerDegLat = 69.0;
        double milesPerDegLon = 69.172 * Math.cos(latRad);

        double dLat = miles / milesPerDegLat;
        double dLon = miles / milesPerDegLon;

        Coordinate[] ring = new Coordinate[] {
                new Coordinate(lon - dLon, lat - dLat),
                new Coordinate(lon + dLon, lat - dLat),
                new Coordinate(lon + dLon, lat + dLat),
                new Coordinate(lon - dLon, lat + dLat),
                new Coordinate(lon - dLon, lat - dLat)
        };
        return GeometryHelper.gf.createPolygon(ring);
    }
}
