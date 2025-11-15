package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.species.Species;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class SpeciesSubmissionView extends VerticalLayout {

    protected final Binder<SpeciesSpotting> binder = new Binder<>();

    protected final Binder<Species> speciesBinder = new Binder<>();

    protected final Binder<Coordinate> coordinateBinder = new Binder<>();

    private final List<HasValueAndElement> components = new ArrayList<>();

    public SpeciesSubmissionView(final SpeciesSpotting speciesSpotting) {
        binder.setBean(speciesSpotting);
        if (speciesSpotting.getLocation() != null) {
            coordinateBinder.setBean(speciesSpotting.getLocation().getCoordinate());
        } else {
            coordinateBinder.setBean(new Coordinate());
        }

        if (speciesSpotting.getSpecies() != null) {
            speciesBinder.setBean(speciesSpotting.getSpecies());
        } else {
            speciesBinder.setBean(new Species());
        }

//        setSizeFull();
//        setAlignItems(Alignment.CENTER);
//        setJustifyContentMode(JustifyContentMode.CENTER);

        setWidth("400px");
        addClassName("form-box"); // optional custom CSS
        getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("padding", "1.5em")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("background-color", "var(--l umo-base-color)");
        add(new H2("Species Spotting"));

        NumberField latitude = new NumberField("Latitude");
        components.add(latitude);
        NumberField longitude = new NumberField("Longitude");
        components.add(longitude);

        TextField speciesNameField = new TextField("Species");
        components.add(speciesNameField);
        TextArea note = new TextArea("Note");
        components.add(note);
        DateTimePicker dateTimePicker = new DateTimePicker("Date and Time Spotted");
        components.add(dateTimePicker);
        NumberField temperature = new NumberField("Temperature");
        components.add(temperature);
        NumberField altitude = new NumberField("Altitude");
        components.add(altitude);

        coordinateBinder.forField(latitude)
                .bind(Coordinate::getY, Coordinate::setY);
        coordinateBinder.forField(longitude)
                .bind(Coordinate::getX, Coordinate::setX);

        speciesBinder.forField(speciesNameField)
                .asRequired()
                .bind(Species::getSpeciesName,
                      Species::setSpeciesName);

        binder.forField(note)
                .bind(SpeciesSpotting::getNote,
                      SpeciesSpotting::setNote);
        binder.forField(temperature)
                .bind(SpeciesSpotting::getTemperatureCelsius,
                      SpeciesSpotting::setTemperatureCelsius);
        binder.forField(dateTimePicker)
                .asRequired()
                .bind(SpeciesSpotting::getObservedAt,
                      SpeciesSpotting::setObservedAt);
        binder.forField(temperature)
                .bind(SpeciesSpotting::getTemperatureCelsius,
                      SpeciesSpotting::setTemperatureCelsius);
        binder.forField(altitude)
                .bind(SpeciesSpotting::getAltitudeMeters,
                      SpeciesSpotting::setAltitudeMeters);


        FormLayout formLayout = new FormLayout(
                speciesNameField,
                note,
                dateTimePicker,
                temperature,
                altitude
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
        this.speciesBinder.setReadOnly(true);
    }

    protected void allowWrite() {
        this.components.forEach(component -> {component.setReadOnly(false);});
        this.binder.setReadOnly(false);
        this.coordinateBinder.setReadOnly(false);
        this.speciesBinder.setReadOnly(false);
    }

}
