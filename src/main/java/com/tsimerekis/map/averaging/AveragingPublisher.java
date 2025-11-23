package com.tsimerekis.map.averaging;

import com.tsimerekis.map.Publisher;
import com.vaadin.flow.component.map.events.MapClickEvent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class AveragingPublisher implements Publisher {

    private ApplicationEventPublisher eventPublisher;

    public AveragingPublisher(@Autowired ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishEvent(Object event) {
        if (event instanceof MapClickEvent mapClickEvent) {
            eventPublisher.publishEvent(new AveragingEvent(mapClickEvent));
        }
    }
}
