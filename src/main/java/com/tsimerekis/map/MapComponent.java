package com.tsimerekis.map;

import com.tsimerekis.map.averaging.event.MapClearEvent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.map.Map;

@Component
@UIScope
public class MapComponent {

    private Map map;

    public MapComponent(@Autowired PublisherFacadeComponent publisher) {
        map = new Map();

        map.addClickEventListener(publisher::publishEvent);
        map.addViewMoveEndEventListener(publisher::publishEvent);
        map.addFeatureClickListener(publisher::publishEvent);
    }

    @EventListener
    private void clearMap(MapClearEvent event) {
        map.getFeatureLayer().removeAllFeatures();
    }

    public Map getMap() {
        return map;
    }
}
