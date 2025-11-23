package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.entity.PollutionReport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class PollutionReportView extends AbstractSubmissionView<PollutionReport> {

    private NumberField pm25Field;
    private NumberField pm10Field;
    private NumberField durationField;
    private TextField sensorTypeField; // if needed
    private TextArea note;
    private DateTimePicker dateTimePicker;

    public PollutionReportView(final PollutionReport report) {
        super(report, "Pollution Report");
    }

    @Override
    protected void createFields() {
        note = new TextArea("Note"); components.add(note);
        dateTimePicker = new DateTimePicker("Date and Time Spotted"); components.add(dateTimePicker);
        pm25Field = new NumberField("PM25"); components.add(pm25Field);
        pm10Field = new NumberField("PM10"); components.add(pm10Field);
        durationField = new NumberField("Duration"); components.add(durationField);
        sensorTypeField = new com.vaadin.flow.component.textfield.TextField("Sensor Type"); components.add(sensorTypeField);
    }

    @Override
    protected void bindFields() {
        binder.forField(note).asRequired().bind(PollutionReport::getNote, PollutionReport::setNote);
        binder.forField(dateTimePicker).asRequired().bind(PollutionReport::getObservedAt, PollutionReport::setObservedAt);
        binder.forField(pm25Field).asRequired().bind(PollutionReport::getPm25, PollutionReport::setPm25);
        binder.forField(pm10Field).asRequired().bind(PollutionReport::getPm10, PollutionReport::setPm10);
        binder.forField(durationField).asRequired().bind(PollutionReport::getDurationHours, PollutionReport::setDurationHours);
        binder.forField(sensorTypeField).asRequired().bind(PollutionReport::getSensorType, PollutionReport::setSensorType);
    }

    @Override
    protected Component[] getFormComponents() {
        return new Component[] {
                note, dateTimePicker, pm25Field, pm10Field, durationField, sensorTypeField
        };
    }

    @Override
    protected org.locationtech.jts.geom.Coordinate extractCoordinate(PollutionReport bean) {
        return bean.getLocation() != null ? bean.getLocation().getCoordinate() : null;
    }
}