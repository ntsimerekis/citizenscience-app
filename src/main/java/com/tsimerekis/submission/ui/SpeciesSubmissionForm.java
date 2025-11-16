package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.service.SubmissionService;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.exception.MissingSpeciesException;
import com.tsimerekis.submission.entity.Species;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.Route;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/species-submission")
public class SpeciesSubmissionForm extends SpeciesSubmissionView {

    private final SubmissionService submissionService;

    private final Integer SRID = 4326;
    private final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
    private final GeometryFactory geometryFactory = new GeometryFactory(pm, SRID);

    public SpeciesSubmissionForm(@Autowired SubmissionService submissionService) {
        super(new SpeciesSpotting());
        allowWrite();

        this.submissionService = submissionService;

        Button save = new Button("Save", e -> {
            saveReport();
        });

        Button reset = new Button("Reset", e -> {
            resetFields();
        });

        add(save, reset);
    }

    private void saveReport() {
        if (binder.validate().isOk() && speciesBinder.validate().isOk()) {
            final SpeciesSpotting spotting = binder.getBean();
            spotting.setLocation(geometryFactory.createPoint(coordinateBinder.getBean()));
            spotting.setSpecies(speciesBinder.getBean());

            try {
                submissionService.save(spotting);
            } catch (MissingSpeciesException me) {
                Notification n = Notification.show("Species not found");
            } catch (InvalidSubmissionException se) {
                Notification n = Notification.show("Invalid Submission");
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

            Notification.show("Submitted");
            resetFields();
        }
    }

    private void resetFields() {
        binder.setBean(new SpeciesSpotting());
        speciesBinder.setBean(new Species());
        coordinateBinder.setBean(new Coordinate());
    }
}
