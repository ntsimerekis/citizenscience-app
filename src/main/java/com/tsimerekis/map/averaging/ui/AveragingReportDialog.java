package com.tsimerekis.map.averaging.ui;

import com.tsimerekis.science.AveragingReport;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;

import java.text.DecimalFormat;

public class AveragingReportDialog extends Dialog {

    private final DecimalFormat df = new DecimalFormat("#,##0.##");

    public AveragingReportDialog(AveragingReport report) {
        setHeaderTitle("Averaging Report");
        setResizable(true);
        setWidth("900px");
        setMaxWidth("95vw");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setSizeFull();

        content.add(buildSummary(report));
        content.add(new H3("Submissions"));
        content.add(buildGrid(report));

        add(content);
    }

    private Component buildSummary(AveragingReport report) {
        // Summary chips across the top
        FlexLayout chips = new FlexLayout();
        chips.getStyle().set("gap", "0.5rem");
//        chips.setOrder(FlexLayout.WrapMode.WRAP);

        chips.add(chip("PM10", fmt(report.getAveragePM10())));
        chips.add(chip("PM2.5", fmt(report.getAveragePM25())));
        chips.add(chip("Temperature (°C)", fmt(report.getAverageTemperature())));
        chips.add(chip("Altitude (m)", fmt(report.getAverageAltitude())));

        chips.add(chip("Has Species", report.speciesSubmissionPresent() ? "Yes" : "No"));
        chips.add(chip("Has Pollution", report.pollutionReportPresent() ? "Yes" : "No"));

        return chips;
    }

    private Component chip(String label, String value) {
        HorizontalLayout wrap = new HorizontalLayout();
        wrap.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
        wrap.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        wrap.getStyle().set("padding", "0.25rem 0.5rem");
        wrap.getStyle().set("background", "var(--lumo-base-color)");
        wrap.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Span l = new Span(label + ":");
        l.getStyle().set("font-weight", "600");
        Span v = new Span(value == null ? "—" : value);
        v.getStyle().set("margin-left", "0.25rem");

        wrap.add(l, v);
        return wrap;
    }

    private String fmt(Double d) {
        return d == null ? null : df.format(d);
    }

    private Grid<Submission> buildGrid(AveragingReport report) {
        Grid<Submission> grid = new Grid<>(Submission.class, false);
        grid.setItems(report.getOrderedSubmissions());
        grid.setAllRowsVisible(true);
        grid.setWidthFull();

        grid.addColumn(s -> {
            if (s instanceof PollutionReport) return "PollutionReport";
            if (s instanceof SpeciesSpotting) return "SpeciesSpotting";
            return s.getClass().getSimpleName();
        }).setHeader("Type").setAutoWidth(true);

        grid.addColumn(s -> {
            if (s instanceof PollutionReport pr && pr.getPm10() != null) return pr.getPm10();
            return null;
        }).setHeader("PM10").setAutoWidth(true);

        grid.addColumn(s -> {
            if (s instanceof PollutionReport pr && pr.getPm25() != null) return pr.getPm25();
            return null;
        }).setHeader("PM2.5").setAutoWidth(true);

        grid.addColumn(s -> {
            if (s instanceof PollutionReport pr && pr.getTemperatureCelsius() != null) return pr.getTemperatureCelsius();
            return null;
        }).setHeader("Temp (°C)").setAutoWidth(true);

        grid.addColumn(s -> {
            if (s instanceof PollutionReport pr && pr.getAltitudeMeters() != null) return pr.getAltitudeMeters();
            if (s instanceof SpeciesSpotting ss && ss.getAltitudeMeters() != null) return ss.getAltitudeMeters();
            return null;
        }).setHeader("Altitude (m)").setAutoWidth(true);

        // Optional: add more domain fields (e.g., species name) if your entities expose them
        return grid;
    }

    /** Helper to open the dialog for a given report. */
    public static AveragingReportDialog openFor(AveragingReport report) {
        AveragingReportDialog d = new AveragingReportDialog(report);
        d.open();
        return d;
    }
}