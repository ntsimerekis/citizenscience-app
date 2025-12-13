package com.tsimerekis.map;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class PublisherFacadeComponent implements Publisher {

    private boolean stealMode = false;

    private Publisher publisher;

    private final ApplicationEventPublisher applicationEventPublisher;

    public PublisherFacadeComponent(@Autowired ApplicationEventPublisher publisher) {
        applicationEventPublisher = publisher;
    }

    @Override
    public void publishEvent(Object o) {
        if (!stealMode) {
            applicationEventPublisher.publishEvent(o);
        } else {
            publisher.publishEvent(o);
        }
    }

    public void startStealing(Publisher publisher) {
        this.publisher = publisher;
        stealMode = true;
    }

    public void stopStealing() {
        stealMode = false;
    }
}
