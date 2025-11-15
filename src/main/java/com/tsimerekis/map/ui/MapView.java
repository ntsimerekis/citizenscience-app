package com.tsimerekis.map.ui;

import com.tsimerekis.map.MapComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/map")
@Menu(order = 100, icon = "vaadin:map-marker", title = "Map View")
public class MapView extends Main {

    private static final Logger log = LogManager.getLogger(MapView.class);

    private final Map map;

    MapView(@Autowired MapComponent mapComponent) {
        //Map
        this.map = mapComponent.getMap();

        Button newSpeciesSpotting = new Button("Species Spotting", e -> {

        });

        Button newPollutionReport = new Button("Pollution Report", e -> {

        });
        add(newSpeciesSpotting, newPollutionReport);


        add(map);
    }



}
