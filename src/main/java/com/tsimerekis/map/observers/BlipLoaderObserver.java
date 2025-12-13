package com.tsimerekis.map.observers;

import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.map.Blip;
import com.tsimerekis.map.FilterComponent;
import com.tsimerekis.map.MapComponent;
import com.tsimerekis.map.averaging.event.MapClearEvent;
import com.tsimerekis.map.ui.GridComponent;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.service.SubmissionService;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Extent;
import com.vaadin.flow.component.map.configuration.layer.FeatureLayer;
import com.vaadin.flow.component.map.events.MapViewMoveEndEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@UIScope
public class BlipLoaderObserver {

    private static final Logger log = LogManager.getLogger(BlipLoaderObserver.class);

    private final java.util.Map<Long, Integer> loaded = new HashMap<>();

    private final SubmissionService submissionService;

    private final Map map;

    private final FilterComponent filterComponent;

    private final GridComponent gridComponent;

    public BlipLoaderObserver(@Autowired MapComponent mapComponent,
                              @Autowired SubmissionService submissionService,
                              @Autowired FilterComponent filterComponent,
                              @Autowired GridComponent gridComponent) {
        this.map = mapComponent.getMap();
        this.submissionService = submissionService;
        this.filterComponent = filterComponent;
        this.gridComponent = gridComponent;
    }

    @EventListener
    public void resetAfterClear(MapClearEvent event) {
        loaded.clear();
    }

    @EventListener
    public void loadBlipsOnDrag(MapViewMoveEndEvent event) {
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

        LinearRing shell = GeometryHelper.gf.createLinearRing(coords);
        Polygon bbox = GeometryHelper.gf.createPolygon(shell, null);

        loadBlips(bbox);
    }

    private void loadBlips(Geometry geometry) {
        final FeatureLayer features = map.getFeatureLayer();

        final List<Submission> submissions = submissionService.findAllByCriteriaAndWithinGeometry(filterComponent.getFilterCriteria(), geometry);

        gridComponent.set(submissions);

        submissions
                .stream()
                .filter(feature -> loaded.putIfAbsent(feature.getId(), 0) == null)
                .map(Blip::createBlip)
                .peek(blip -> Notification.show(blip.getId()))
                .forEach(features::addFeature);

        log.debug("Blips loaded");
    }
}
