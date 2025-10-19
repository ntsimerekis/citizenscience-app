package com.tsimerekis.map.ui;

import com.tsimerekis.map.Blip;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.map.configuration.layer.FeatureLayer;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.locationtech.jts.geom.Geometry;

@Route("/map")
@Menu(order = 100, icon = "vaadin:map-marker", title = "Map View")
public class MapView extends Main {

    private final Map map;

    MapView() {
        //Map
        map = new Map();

        // 1) Put markers on the preconfigured FeatureLayer
        FeatureLayer features = map.getFeatureLayer();

//        MarkerFeature hawk = new MarkerFeature(new Coordinate(34.05882130402509, -117.81997648597421)); // lon, lat
        Blip hawk = Blip.createBlip(-121.92163, 37.36821);

        features.addFeature(hawk);

        // 2) Listen for clicks on features (markers, polygons, etc.)
        map.addFeatureClickListener(ev -> {
            var feature = ev.getFeature();
            if (feature instanceof Blip blip) {
                String id = blip.getId(); // retrieve your tag/ID
                // TODO: open your popup / dialog / side panel here
                Notification.show("Marker clicked: " + id);
            }
        });

        add(map);
    }

    private void loadBlips(Geometry geometry) {

    }

}
