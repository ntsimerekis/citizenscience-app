package com.tsimerekis.map.ui;

import com.tsimerekis.map.Blip;
import com.tsimerekis.submission.SubmissionService;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.ui.PollutionReportView;
import com.tsimerekis.submission.ui.SpeciesSubmissionView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Extent;
import com.vaadin.flow.component.map.configuration.layer.FeatureLayer;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("/map")
@Menu(order = 100, icon = "vaadin:map-marker", title = "Map View")
public class MapView extends Main {

    private final static PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);

    private final static GeometryFactory gf = new GeometryFactory(pm, 4326);

    private static final Logger log = LogManager.getLogger(MapView.class);

    private final SubmissionService submissionService;

    private final Map map;

    MapView(@Autowired SubmissionService submissionService) {
        //Map
        this.map = new Map();
        this.submissionService = submissionService;

        map.addViewMoveEndEventListener(event -> {
            final Extent extent = event.getExtent();
            final double left = extent.getMinX();
            final double top = extent.getMinY();
            final double right = extent.getMaxX();
            final double bottom = extent.getMaxY();

            Coordinate[] coords = new Coordinate[] {
                    new Coordinate(left,  bottom), // SW
                    new Coordinate(right, bottom), // SE
                    new Coordinate(right, top),    // NE
                    new Coordinate(left,  top),    // NW
                    new Coordinate(left,  bottom)  // back to SW
            };

            LinearRing shell = gf.createLinearRing(coords);
            Polygon bbox = gf.createPolygon(shell, null);

            loadBlips(bbox);
        });

        map.addClickEventListener(event -> {
            final var vaadinCoord = event.getCoordinate();
            final Geometry squareRadius = roughMilesSquare(vaadinCoord.getY(), vaadinCoord.getX(), 50);
            final int submissionsFound = submissionService.findWithinGeometry(squareRadius).size();

            Notification.show(Integer.toString(submissionsFound));
        });

        map.addFeatureClickListener(ev -> {
            var feature = ev.getFeature();
            if (feature instanceof Blip blip) {
                final Long id;
                try {
                    id = Long.valueOf(blip.getId());
                } catch (NumberFormatException e) {
                    Notification.show("Invalid blip ID");
                    return;
                }
                Notification.show(String.valueOf(id));

                Optional<Submission> submission = submissionService.findById(id);
                submission.ifPresentOrElse( s -> {
                    if (s instanceof PollutionReport pollutionReport) {
                        Notification.show("Opening pollution report " + id);
                        openSubmissionWindow(pollutionReport);
                    } else if (s instanceof SpeciesSpotting speciesSpotting) {
                        Notification.show("Opening species spotting " + id);
                        openSubmissionWindow(speciesSpotting);
                    }
                    },
                    () -> {
                        Notification.show("Unknown submission type");
                    }
                );
            }
        });

        Button newSpeciesSpotting = new Button("Species Spotting", e -> {

        });

        Button newPollutionReport = new Button("Pollution Report", e -> {

        });
        add(newSpeciesSpotting, newPollutionReport);

        add(map);
    }

    private void loadBlips(Geometry geometry) {
        final FeatureLayer features = map.getFeatureLayer();

        submissionService.findWithinGeometry(geometry)
                .stream()
                .map(Blip::createBlip)
                .forEach(features::addFeature);

        log.debug("Blips loaded");
    }

    private Dialog submissionDialog() {
        Dialog dialog = new Dialog();
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING); // optional
        dialog.setHeaderTitle("Person details");
        dialog.setModal(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("520px");
        dialog.setMaxWidth("90vw");
        dialog.getElement().setAttribute("aria-label", "Person details");

        return dialog;
    }

    private void openSubmissionWindow(PollutionReport pollutionReport) {
        final Dialog dialog = submissionDialog();
        dialog.add(new PollutionReportView(pollutionReport));

        dialog.open();
    }

    private void openSubmissionWindow(SpeciesSpotting speciesSpotting) {
        final Dialog dialog = submissionDialog();
        dialog.add(new SpeciesSubmissionView(speciesSpotting));

        dialog.open();
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
        return gf.createPolygon(ring);
    }
}
