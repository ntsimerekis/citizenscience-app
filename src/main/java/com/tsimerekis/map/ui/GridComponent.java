package com.tsimerekis.map.ui;

import com.tsimerekis.map.averaging.event.RowClickEvent;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class GridComponent {

    private final Grid<Submission> grid = new Grid<>(Submission.class, false);

    public GridComponent(@Autowired ApplicationEventPublisher publisher) {
        grid.addCellFocusListener(event -> {});
        grid.addColumn(Submission::getId).setHeader("Id");
        grid.addColumn(Submission::getObservedAt).setHeader("Created At");
        grid.addColumn(Submission::getNote).setHeader("Note");
        grid.addColumn(Submission::getTemperatureCelsius).setHeader("Temperature C");
        grid.addColumn(Submission::getAltitudeMeters).setHeader("Altitude M");

        grid.addCellFocusListener(event -> {
            event.getItem().ifPresent(s -> {
                publisher.publishEvent(new RowClickEvent(s));
                grid.asSingleSelect().clear();
                grid.asMultiSelect().clear();
            });
        });
    }

    public void set(List<Submission> submissions) {
        grid.setItems(submissions);
    }

    public Grid<Submission> getGrid() {
        return grid;
    }

}
