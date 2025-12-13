package com.tsimerekis.submission.ui;

import com.tsimerekis.bucket.BucketService;
import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.exception.MissingSpeciesException;
import com.tsimerekis.submission.service .SubmissionService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SubmissionViewFactory {

    private final SubmissionService submissionService;

    private final BucketService bucketService;

    public SubmissionViewFactory(@Autowired SubmissionService submissionService,
                                 @Autowired BucketService bucketService) {
        this.submissionService = submissionService;
        this.bucketService = bucketService;
    }

    public VerticalLayout createSubmissionView(Submission submission) {
        if (submission instanceof PollutionReport pollutionReport) {
            return new PollutionReportView(pollutionReport);
        } else if (submission instanceof SpeciesSpotting spotting) {
            Image image = new Image(bucketService.getSignedDownloadURL("submission/" + submission.getId() + "/photo").toString(), "My Alt Image");

            final SpeciesSubmissionView speciesSubmissionView = new SpeciesSubmissionView(spotting);
            speciesSubmissionView.add(image);

            return speciesSubmissionView;
        }

        return null;
    }

    public VerticalLayout createSubmissionForm(Submission submission, Dialog dialog) {
        final VerticalLayout form = new VerticalLayout();

        final AbstractSubmissionView<?> submissionView;

        if (submission instanceof PollutionReport pollutionReport) {

            submissionView = new PollutionReportView(pollutionReport);
            submissionView.allowWrite();

            form.add(submissionView);
            form.add(new Button("Save", s -> {
                pollutionReport.setLocation(submissionView.getCoordinate());

                try {
                    submissionService.save(pollutionReport);
                    Notification.show("Submitted");
                } catch (InvalidSubmissionException se) {
                    Notification n = Notification.show("Invalid Submission");
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                dialog.close();
            }));
        } else if (submission instanceof SpeciesSpotting spotting) {
            final SpeciesSubmissionView speciesSubmissionView = new SpeciesSubmissionView(spotting);
            submissionView = speciesSubmissionView;
            speciesSubmissionView.allowWrite();
            speciesSubmissionView.setSpeciesSuggestions(submissionService::ngramSearchSpecies);

            form.add(speciesSubmissionView);

            final byte[][] bytes = new byte[1][1];
            AtomicReference<String> contentType = new AtomicReference<>();

            InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory(
                    (metadata, data) -> {
                        // Get other information about the file.
                        String fileName = metadata.fileName();
                        String mimeType = metadata.contentType();
                        long contentLength = metadata.contentLength();

                        // Do something with the file data...
                        // processFile(data, fileName);
                        contentType.set(mimeType);
                        bytes[0] = data;
                    });
            Upload upload = new Upload(inMemoryHandler);

            form.add(upload);

            form.add(new Button("Save", s -> {
                spotting.setLocation(speciesSubmissionView.getCoordinate());
                spotting.setSpecies(speciesSubmissionView.getSpecies());

                try {
                    final Submission savedSubmission = submissionService.save(spotting);

                    bucketService.uploadSubmissionPhoto(savedSubmission.getId(), new ByteArrayInputStream(bytes[0]), contentType.get());
                    Notification.show("Submitted");
                } catch (MissingSpeciesException me) {
                    Notification n = Notification.show("Species not found");
                } catch (InvalidSubmissionException se) {
                    Notification n = Notification.show("Invalid Submission");
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                dialog.close();
            }));
        } else {
            //Default to pollution as default implementation
            submissionView = new PollutionReportView(new PollutionReport());
        }

        form.add(locationSelectorWidget(submissionView, submission));

        return form;
    }

    private com.vaadin.flow.component.Component locationSelectorWidget(AbstractSubmissionView<?> form, Submission submission) {
        final Map map = new Map();

        // Set Center and Zoom to ensure the marker is visible
        map.setCenter(new Coordinate(33.261657, -79.983697));
//        map.setZoom(12);

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

            final Coordinate vaadinClickedCoord = event.getCoordinate();
            final org.locationtech.jts.geom.Coordinate jtsCoord = submission.getLocation().getCoordinate();

            jtsCoord.setX(vaadinClickedCoord.getX());
            jtsCoord.setY(vaadinClickedCoord.getY());

            form.refreshFields();
        });

        map.addFeatureDropListener(event -> {
            MarkerFeature droppedMarker = (MarkerFeature) event.getFeature();
            // ... (rest of the drop logic)
            Coordinate endCoordinates = event.getCoordinate();
            submission.setLocation(GeometryHelper.getJTSPoint(endCoordinates));
            Notification.show("Marker \"" + droppedMarker.getId() + "\" dragged to " + endCoordinates);
        });

        return map;
    }
}
