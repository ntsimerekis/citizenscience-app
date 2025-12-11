package com.tsimerekis.submission.ui;

import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.exception.MissingSpeciesException;
import com.tsimerekis.submission.service.SubmissionService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmissionViewFactory {

    private SubmissionService submissionService;

    public SubmissionViewFactory(@Autowired SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    public VerticalLayout createSubmissionView(Submission submission) {
        if (submission instanceof PollutionReport pollutionReport) {
            return new PollutionReportView(pollutionReport);
        } else if (submission instanceof SpeciesSpotting spotting) {
            return new SpeciesSubmissionView(spotting);
        }

        return null;
    }

    public VerticalLayout createSubmissionForm(Submission submission, Dialog dialog) {
        final VerticalLayout form = new VerticalLayout();

        if (submission instanceof PollutionReport pollutionReport) {

            final PollutionReportView pollutionReportView = new PollutionReportView(pollutionReport);
            pollutionReportView.allowWrite();

            form.add(pollutionReportView);
            form.add(new Button("Save", s -> {
                pollutionReport.setLocation(pollutionReportView.getCoordinate());

                try {
                    submissionService.save(pollutionReport);
                } catch (InvalidSubmissionException se) {
                    Notification n = Notification.show("Invalid Submission");
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                Notification.show("Submitted");
                dialog.close();
            }));
        } else if (submission instanceof SpeciesSpotting spotting) {
            final SpeciesSubmissionView speciesSubmissionView = new SpeciesSubmissionView(spotting);
            speciesSubmissionView.allowWrite();

            form.add(speciesSubmissionView);
            form.add(new Button("Save", s -> {
                spotting.setLocation(speciesSubmissionView.getCoordinate());
                spotting.setSpecies(speciesSubmissionView.getSpecies());

                try {
                    submissionService.save(spotting);
                } catch (MissingSpeciesException me) {
                    Notification n = Notification.show("Species not found");
                } catch (InvalidSubmissionException se) {
                    Notification n = Notification.show("Invalid Submission");
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                Notification.show("Submitted");
                dialog.close();
            }));
        }
        addLocationSelectorWidget(form, submission);

        return form;
    }

    private void addLocationSelectorWidget(VerticalLayout form, Submission submission) {
        final Map map = new Map();

        // Set Center and Zoom to ensure the marker is visible
        map.setCenter(new Coordinate(33.261657, -79.983697));
        map.setZoom(12);

        // Define the initial coordinates for the marker
        Coordinate initialCoordinate = new Coordinate(33.261657, -79.983697);

        final MarkerFeature marker = new MarkerFeature(initialCoordinate);
        marker.setId("draggable-marker");
        marker.setDraggable(true);
        marker.setText("Drag me");
        map.getFeatureLayer().addFeature(marker);

        map.setWidth(512, Unit.PIXELS);
        map.setHeight(512, Unit.PIXELS);

        // ... (rest of the code for click/drop listeners is fine)
        map.addClickEventListener(event -> {
            final MarkerFeature newMarker = new MarkerFeature(event.getCoordinate());
            map.getFeatureLayer().addFeature(newMarker);
        });

        map.addFeatureDropListener(event -> {
            MarkerFeature droppedMarker = (MarkerFeature) event.getFeature();
            // ... (rest of the drop logic)
            Coordinate endCoordinates = event.getCoordinate();
            submission.setLocation(GeometryHelper.getJTSPoint(endCoordinates));
            Notification.show("Marker \"" + droppedMarker.getId() + "\" dragged to " + endCoordinates);
        });

        form.add(map);
    }
}
