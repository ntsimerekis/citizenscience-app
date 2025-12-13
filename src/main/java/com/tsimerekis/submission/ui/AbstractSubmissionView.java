package com.tsimerekis.submission.ui;

import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubmissionView<T extends Submission> extends VerticalLayout {
    protected final Binder<T> binder = new Binder<>();
    protected final Binder<Coordinate> coordinateBinder = new Binder<>();
    protected final List<HasValueAndElement> components = new ArrayList<>();

    protected NumberField latitude;
    protected NumberField longitude;

    protected AbstractSubmissionView(T bean, String title) {
        binder.setBean(bean);
        Coordinate existing = extractCoordinate(bean);
        if (existing != null) {
            coordinateBinder.setBean(existing);
        } else {
            final Coordinate newCoord = new Coordinate();
            coordinateBinder.setBean(newCoord);
            bean.setLocation(GeometryHelper.gf.createPoint(newCoord));
        }

        configureStyle(title);
        createCoordinateFields();
        createFields();        // hook for subclasses to create UI fields
        bindCoordinateFields();
        bindFields();          // hook for subclasses to bind fields to model
        add(createFormLayout());
        setReadOnly();
    }

    private void configureStyle(String title) {
        setWidth("400px");
        addClassName("form-box");
        getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("padding", "1.5em")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("background-color", "var(--lumo-base-color)");
        add(new H2(title));
    }

    private void createCoordinateFields() {
        latitude = new NumberField("Latitude");
        components.add(latitude);
        longitude = new NumberField("Longitude");
        components.add(longitude);
    }

    private void bindCoordinateFields() {
        coordinateBinder.forField(latitude)
                .bind(Coordinate::getY, Coordinate::setY);
        coordinateBinder.forField(longitude)
                .bind(Coordinate::getX, Coordinate::setX);
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout(getFormComponents());
        formLayout.addFormRow(latitude, longitude);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400px", 2)
        );
        return formLayout;
    }

    public void setReadOnly() {
        this.components.forEach(c -> c.setReadOnly(true));
        this.binder.setReadOnly(true);
        this.coordinateBinder.setReadOnly(true);
    }

    public void allowWrite() {
        this.components.forEach(c -> c.setReadOnly(false));
        this.binder.setReadOnly(false);
        this.coordinateBinder.setReadOnly(false);
    }

    public void refreshFields() {
        binder.refreshFields();
        coordinateBinder.refreshFields();
    }

    //A little messy
    public Point getCoordinate() {
        return GeometryHelper.gf.createPoint(coordinateBinder.getBean());
    }

    // subclass responsibilities
    protected abstract void createFields();
    protected abstract void bindFields();
    protected abstract Component[] getFormComponents();
    protected abstract Coordinate extractCoordinate(T bean);
}