package com.tsimerekis.map.ui;

import com.tsimerekis.map.FilterComponent;
import com.tsimerekis.map.MapComponent;
import com.tsimerekis.map.averaging.AveragingComponent;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.repository.FilterCriteria;
import com.tsimerekis.submission.ui.SubmissionViewFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/map")
@Menu(order = 100, icon = "vaadin:map-marker", title = "Map View")
public class MapView extends Main {

    private static final Logger log = LogManager.getLogger(MapView.class);

    private final Map map;

    private final FilterComponent filterComponent;

    MapView(@Autowired MapComponent mapComponent,
            @Autowired FilterComponent filterComponent,
            @Autowired AveragingComponent averagingComponent,
            @Autowired SubmissionViewFactory submissionViewFactory) {

        //Map
        this.map = mapComponent.getMap();
        this.filterComponent = filterComponent;

        Button newSpeciesSpotting = new Button("Species Spotting", e -> {
            final Dialog dialog = new Dialog("Species Spotting");
            final VerticalLayout view = submissionViewFactory.createSubmissionForm(new SpeciesSpotting(), dialog);

            dialog.add(view);
            dialog.open();
        });

        Button newPollutionReport = new Button("Pollution Report", e -> {
            final Dialog dialog = new Dialog("Pollution Report");
            final VerticalLayout view = submissionViewFactory.createSubmissionForm(new PollutionReport(), dialog);

            dialog.add(view);
            dialog.open();
        });

        Button newFilterCriteria = new Button("Filter Criteria", e -> {
            Dialog dialog = DialogHelper.submissionDialog();

            FilterCriteria filterCriteria = filterComponent.getFilterCriteria();

            dialog.add(new FilterBox(filterCriteria));
            dialog.open();
            Notification.show("Filter Criteria clicked");
        });

        Button newAveragingReport = new Button("Averaging Report", e -> {
            averagingComponent.startAveragingMode();
        });

        add(newSpeciesSpotting, newPollutionReport, newFilterCriteria, newAveragingReport);

        add(map);
    }
}
