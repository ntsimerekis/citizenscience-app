package com.tsimerekis.map.averaging;

import com.tsimerekis.geometry.GeometryHelper;
import com.tsimerekis.map.FilterComponent;
import com.tsimerekis.map.PublisherComponent;
import com.tsimerekis.science.AveragingReport;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.repository.FilterCriteria;
import com.tsimerekis.submission.service.SubmissionService;
import com.vaadin.flow.component.map.events.MapClickEvent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class AveragingComponent {

    private boolean averagingMode = false;

    private PublisherComponent publisherComponent;

    private AveragingPublisher averagingPublisher;

    private FilterComponent filterComponent;

    private SubmissionService submissionService;

    private ApplicationEventPublisher applicationEventPublisher;

    private Point[] points;

    public AveragingComponent(@Autowired PublisherComponent publisherComponent,
                              @Autowired AveragingPublisher averagingPublisher,
                              @Autowired FilterComponent filterComponent,
                              @Autowired SubmissionService submissionService,
                              @Autowired ApplicationEventPublisher eventPublisher) {
        this.publisherComponent = publisherComponent;
        this.averagingPublisher = averagingPublisher;
        this.filterComponent = filterComponent;
        this.submissionService = submissionService;
        this.applicationEventPublisher = eventPublisher;
    }

    public boolean startAveragingMode() {
        if (!averagingMode) {
            averagingMode = true;
            publisherComponent.startStealing(averagingPublisher);
            points = new Point[2];
        }

        return !averagingMode;
    }

    @EventListener
    public void getBoxClick(AveragingEvent event) {
        if (averagingMode) {
            final MapClickEvent mapClickEvent = event.getMapClickEvent();

            if (points[0] == null) {
                points[0] = GeometryHelper.getJTSPoint(mapClickEvent.getCoordinate());
            } else {
                if (points[1] == null) {
                    points[1] = GeometryHelper.getJTSPoint(mapClickEvent.getCoordinate());
                }
            }

            if (points[0] != null && points[1] != null) {
                stopAveragingMode();
            }
        }
    }

    public void publishCurrentReport() {
        final Geometry rectangle = GeometryHelper.rectangleFromPoints(points[0], points[1]);
        final FilterCriteria filterCriteria = filterComponent.getFilterCriteria();

        final List<Submission> submissions = submissionService.findAllByCriteriaAndWithinGeometry(filterCriteria, rectangle);

        final AveragingReport report = new AveragingReport();
        report.addReports(submissions);

        applicationEventPublisher.publishEvent(new AveragingDoneEvent(report));
    }

    private void stopAveragingMode() {
        //STOP STEALING!
        publisherComponent.stopStealing();

        publishCurrentReport();
        points = new Point[2];

        averagingMode = false;
    }
}
