package com.tsimerekis.map.averaging;

import com.vaadin.flow.component.map.events.MapClickEvent;

public class AveragingEvent {
    private final MapClickEvent mapClickEvent;

    public AveragingEvent(MapClickEvent mapClickEvent) {
        this.mapClickEvent = mapClickEvent;
    }

    public MapClickEvent getMapClickEvent() {
        return mapClickEvent;
    }
}
