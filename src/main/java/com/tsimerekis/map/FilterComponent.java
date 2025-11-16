package com.tsimerekis.map;

import com.tsimerekis.submission.repository.FilterCriteria;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class FilterComponent {

    private final FilterCriteria filterCriteria;

    public FilterComponent() {
        filterCriteria = new FilterCriteria();
        Notification.show("Filter criteria");
    }

    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }
}
