package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Species;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class SpeciesSubmissionView extends AbstractSubmissionView<SpeciesSpotting> {
    protected Binder<Species> speciesBinder;

    private TextField speciesNameField;
    private TextArea note;
    private DateTimePicker dateTimePicker;
    private NumberField temperature;
    private NumberField altitude;

    public SpeciesSubmissionView(final SpeciesSpotting speciesSpotting) {
        super(speciesSpotting, "Species Spotting");
        if (speciesSpotting.getSpecies() != null) {
            speciesBinder.setBean(speciesSpotting.getSpecies());
        } else {
            speciesBinder.setBean(new Species());
        }
    }

    @Override
    protected void createFields() {
        speciesNameField = new TextField("Species"); components.add(speciesNameField);
        note = new TextArea("Note"); components.add(note);
        dateTimePicker = new DateTimePicker("Date and Time Spotted"); components.add(dateTimePicker);
        temperature = new NumberField("Temperature"); components.add(temperature);
        altitude = new NumberField("Altitude"); components.add(altitude);
    }

    @Override
    protected void bindFields() {
        //java is weird man
        speciesBinder = new Binder<>();

        speciesBinder.forField(speciesNameField).asRequired().bind(Species::getSpeciesName, Species::setSpeciesName);

        binder.forField(note).bind(SpeciesSpotting::getNote, SpeciesSpotting::setNote);
        binder.forField(dateTimePicker).asRequired().bind(SpeciesSpotting::getObservedAt, SpeciesSpotting::setObservedAt);
        binder.forField(temperature).bind(SpeciesSpotting::getTemperatureCelsius, SpeciesSpotting::setTemperatureCelsius);
        binder.forField(altitude).bind(SpeciesSpotting::getAltitudeMeters, SpeciesSpotting::setAltitudeMeters);
    }

    @Override
    protected Component[] getFormComponents() {
        return new Component[] {
                speciesNameField, note, dateTimePicker, temperature, altitude
        };
    }

    @Override
    protected org.locationtech.jts.geom.Coordinate extractCoordinate(SpeciesSpotting bean) {
        return bean.getLocation() != null ? bean.getLocation().getCoordinate() : null;
    }
}