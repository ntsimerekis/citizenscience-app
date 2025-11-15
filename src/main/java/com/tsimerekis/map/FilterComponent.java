package com.tsimerekis.map;

import com.tsimerekis.submission.FilterCriteria;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class FilterComponent {

    private FilterCriteria filterCriteria;

    public FilterComponent() {
        filterCriteria = new FilterCriteria();
    }

    public void setFilterCriteria(FilterCriteria filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }
}
