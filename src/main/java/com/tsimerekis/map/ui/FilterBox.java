package com.tsimerekis.map.ui;

import com.tsimerekis.map.FilterComponent;
import com.tsimerekis.map.MapComponent;
import com.tsimerekis.submission.entity.SubmissionType;
import com.tsimerekis.submission.repository.FilterCriteria;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.RangeInput;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.text.DecimalFormat;

public class FilterBox extends VerticalLayout {

    protected final Binder<FilterCriteria> binder = new Binder<>();
    private final DecimalFormat intFormat = new DecimalFormat("#");

    public FilterBox(final FilterCriteria criteria) {

        // bind bean so UI changes propagate to the criteria instance
        binder.setBean(criteria);

        final FormLayout form = new FormLayout();

        // Date/time pickers for observed date range (convert LocalDateTime <-> Instant)
        final DateTimePicker lowestObserved = new DateTimePicker();
        final DateTimePicker highestObserved = new DateTimePicker();
        Converter<LocalDateTime, Instant> ldtToInstant = new Converter<LocalDateTime, Instant>() {
            @Override
            public Result<Instant> convertToModel(LocalDateTime value, ValueContext context) {
                if (value == null) {
                    return Result.ok(null);
                }
                return Result.ok(value.atZone(ZoneId.systemDefault()).toInstant());
            }

            @Override
            public LocalDateTime convertToPresentation(Instant value, ValueContext context) {
                if (value == null) {
                    return null;
                }
                return LocalDateTime.ofInstant(value, ZoneId.systemDefault());
            }
        };
        binder.forField(lowestObserved).withConverter(ldtToInstant)
                .bind(FilterCriteria::getLowestObservedDate, FilterCriteria::setLowestObservedDate);
        binder.forField(highestObserved).withConverter(ldtToInstant)
                .bind(FilterCriteria::getHighestObservedDate, FilterCriteria::setHighestObservedDate);

        // Submission type combo box
        final ComboBox<String> submissionType = new ComboBox<>("Submission Type");
        submissionType.setItems(SubmissionType.getAllTypes());
        binder.forField(submissionType)
                .bind(FilterCriteria::getSubmissionType, FilterCriteria::setSubmissionType);

        // PM2.5 sliders (with numeric label)
        final RangeInput minPM25Slider = newPMSlider();
        final HorizontalLayout minPM25Layout = sliderWithLabel(minPM25Slider);
        final RangeInput maxPM25Slider = newPMSlider();
        final HorizontalLayout maxPM25Layout = sliderWithLabel(maxPM25Slider);
        binder.forField(minPM25Slider)
                .bind(FilterCriteria::getMinPM25, FilterCriteria::setMinPM25);
        binder.forField(maxPM25Slider)
                .bind(FilterCriteria::getMaxPM25, FilterCriteria::setMaxPM25);

        // PM10 sliders (with numeric label)
        final RangeInput minPM10Slider = newPMSlider();
        final HorizontalLayout minPM10Layout = sliderWithLabel(minPM10Slider);
        final RangeInput maxPM10Slider = newPMSlider();
        final HorizontalLayout maxPM10Layout = sliderWithLabel(maxPM10Slider);
        binder.forField(minPM10Slider)
                .bind(FilterCriteria::getMinPM10, FilterCriteria::setMinPM10);
        binder.forField(maxPM10Slider)
                .bind(FilterCriteria::getMaxPM10, FilterCriteria::setMaxPM10);

        // Temperature sliders (with numeric label)
        final RangeInput minTempSlider = newPMSlider();
        final HorizontalLayout minTempLayout = sliderWithLabel(minTempSlider);
        final RangeInput maxTempSlider = newPMSlider();
        final HorizontalLayout maxTempLayout = sliderWithLabel(maxTempSlider);
        binder.forField(minTempSlider)
                .bind(FilterCriteria::getMinTemperature, FilterCriteria::setMinTemperature);
        binder.forField(maxTempSlider)
                .bind(FilterCriteria::getMaxTemperature, FilterCriteria::setMaxTemperature);

        // Add items to form (use the layouts containing slider + number)
        form.addFormItem(lowestObserved, "Lowest Observed Date");
        form.addFormItem(highestObserved, "Highest Observed Date");
        form.addFormItem(submissionType, "Submission Type");
        form.addFormItem(minPM25Layout, "Minimum PM2.5");
        form.addFormItem(maxPM25Layout, "Maximum PM2.5");
        form.addFormItem(minPM10Layout, "Minimum PM10");
        form.addFormItem(maxPM10Layout, "Maximum PM10");
        form.addFormItem(minTempLayout, "Minimum Temperature");
        form.addFormItem(maxTempLayout, "Maximum Temperature");

        add(form);
    }

    private HorizontalLayout sliderWithLabel(RangeInput slider) {
        final Span valueLabel = new Span(formatValue(slider.getValue()));
        valueLabel.getStyle().set("min-width", "3rem").set("display", "inline-block");
        // update label when slider changes
        slider.addValueChangeListener(e -> valueLabel.setText(formatValue(e.getValue())));
        // initialize label if binder/criteria later changes the slider value, keep listening
        // layout contains slider and numeric label
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.add(slider, valueLabel);
        return layout;
    }

    private String formatValue(Double v) {
        if (v == null) {
            return "-";
        }
        // show integer-like value (no decimals) because step is 5.0
        return intFormat.format(v);
    }

    private static RangeInput newPMSlider() {
        final RangeInput slider = new RangeInput();
        slider.setMin(0.0);
        slider.setMax(100.0);
        slider.setStep(5.0);
        return slider;
    }
}