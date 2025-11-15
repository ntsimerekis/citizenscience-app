package com.tsimerekis.submission.ui;

import com.vaadin.flow.component.HasValueAndElement;
import org.locationtech.jts.geom.Coordinate;

import com.tsimerekis.submission.entity.PollutionReport;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class PollutionReportView extends VerticalLayout {

    protected final Binder<PollutionReport> binder = new Binder<>();

    protected final Binder<Coordinate> coordinateBinder = new Binder<>();

    private final List<HasValueAndElement> components = new ArrayList<>();

    public PollutionReportView(final PollutionReport report) {
        binder.setBean(report);
        if (report.getLocation() != null) {
            coordinateBinder.setBean(report.getLocation().getCoordinate());
        } else {
            coordinateBinder.setBean(new Coordinate());
        }

        setWidth("400px");
        addClassName("form-box"); // optional custom CSS
        getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("padding", "1.5em")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("background-color", "var(--l umo-base-color)");
        add(new H2("Pollution Report"));

        NumberField latitude = new NumberField("Latitude");
        components.add(latitude);
        NumberField longitude = new NumberField("Longitude");
        components.add(longitude);

        TextArea note = new TextArea("Note");
        components.add(note);
        DateTimePicker dateTimePicker = new DateTimePicker("Date and Time Spotted");
        components.add(dateTimePicker);

        NumberField pm25Field = new NumberField("PM25");
        components.add(pm25Field);
        NumberField pm10Field = new NumberField("PM10");
        components.add(pm10Field);
        NumberField durationField = new NumberField("Duration");
        components.add(durationField);
        TextField sensorTypeField = new TextField("Sensor Type");
        components.add(sensorTypeField);

        coordinateBinder.forField(latitude)
                .bind(Coordinate::getY, Coordinate::setY);
        coordinateBinder.forField(longitude)
                        .bind(Coordinate::getX, Coordinate::setX);

        binder.forField(note)
                .asRequired()
                .bind(PollutionReport::getNote, PollutionReport::setNote);
        binder.forField(dateTimePicker)
                .asRequired()
                .bind(PollutionReport::getObservedAt, PollutionReport::setObservedAt);
        binder.forField(pm25Field)
                .asRequired()
                .bind(PollutionReport::getPM25, PollutionReport::setPM25);
        binder.forField(pm10Field)
                .asRequired()
                .bind(PollutionReport::getPM10, PollutionReport::setPM10);
        binder.forField(durationField)
                .asRequired()
                .bind(PollutionReport::getDurationHours, PollutionReport::setDurationHours);
        binder.forField(sensorTypeField)
                .asRequired()
                .bind(PollutionReport::getSensorType, PollutionReport::setSensorType);

        FormLayout formLayout = new FormLayout(
                note,
                dateTimePicker,
                pm25Field,
                pm10Field,
                durationField,
                sensorTypeField
        );
        formLayout.addFormRow(latitude, longitude);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400px", 2)
        );
        add(formLayout);

        setReadOnly();
    }

    protected void setReadOnly() {
        this.components.forEach(component -> {component.setReadOnly(true);});
        this.binder.setReadOnly(true);
        this.coordinateBinder.setReadOnly(true);
    }

    protected void allowWrite() {
        this.components.forEach(component -> {component.setReadOnly(false);});
        this.binder.setReadOnly(false);
        this.coordinateBinder.setReadOnly(false);
    }
}
