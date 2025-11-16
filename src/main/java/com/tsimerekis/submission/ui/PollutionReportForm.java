package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.service.SubmissionService;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.Route;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/pollution-report")
public class PollutionReportForm extends PollutionReportView {

    private SubmissionService submissionService;

    private final Integer SRID = 4326;
    private final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
    private final GeometryFactory geometryFactory = new GeometryFactory(pm, SRID);

    public PollutionReportForm(@Autowired SubmissionService submissionService) {
        super(new PollutionReport());
        allowWrite();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

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
        if (binder.validate().isOk() && coordinateBinder.validate().isOk()) {
            final PollutionReport pollutionReport = binder.getBean();
            pollutionReport.setLocation(geometryFactory.createPoint(coordinateBinder.getBean()));
            try {
                submissionService.save(pollutionReport);
            } catch (InvalidSubmissionException se) {
                Notification n = Notification.show("Invalid Submission");
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

            Notification.show("Submitted");
            resetFields();
        }
    }

    private void resetFields() {
        binder.setBean(new PollutionReport());
        coordinateBinder.setBean(new Coordinate());
    }
}
