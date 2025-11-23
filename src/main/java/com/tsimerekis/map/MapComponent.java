package com.tsimerekis.map;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.map.Map;

@Component
@UIScope
public class MapComponent {

    private Map map;

    public MapComponent(@Autowired PublisherComponent publisher) {
        map = new Map();

        map.addClickEventListener(publisher::publishEvent);
        map.addViewMoveEndEventListener(publisher::publishEvent);
        map.addFeatureClickListener(publisher::publishEvent);
    }

    public Map getMap() {
        return map;
    }
}
