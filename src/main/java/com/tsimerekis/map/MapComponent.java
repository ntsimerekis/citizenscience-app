package com.tsimerekis.map;

import com.tsimerekis.map.observers.BlipClickObserver;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.map.Map;

@Component
@UIScope
public class MapComponent {

    private final Map map;

    public MapComponent(@Autowired ApplicationEventPublisher publisher) {
        map = new Map();

        map.addClickEventListener(publisher::publishEvent);
        map.addViewMoveEndEventListener(publisher::publishEvent);
        map.addFeatureClickListener(publisher::publishEvent);
        map.addViewMoveEndEventListener(publisher::publishEvent);
    }

    public void clearMap() {
        map.getFeatureLayer().removeAllFeatures();
    }

    public Map getMap() {
        return map;
    }
}
