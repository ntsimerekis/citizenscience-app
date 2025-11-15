package com.tsimerekis.map.observers;

import com.tsimerekis.map.Blip;
import com.tsimerekis.map.proxy.SubmissionProxy;
import com.tsimerekis.submission.SubmissionService;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.ui.PollutionReportView;
import com.tsimerekis.submission.ui.SpeciesSubmissionView;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.map.events.MapFeatureClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
//@Scope("prototype")
@UIScope
public class BlipClickObserver {

    private final SubmissionProxy submissionProxy;

    public BlipClickObserver(@Autowired SubmissionProxy submissionProxy) {
        this.submissionProxy = submissionProxy;
    }

    @EventListener
    public void onBlipClick(MapFeatureClickEvent event) {
        var feature = event.getFeature();

        if (feature instanceof Blip blip) {
            final Long id;
            try {
                id = Long.valueOf(blip.getId());
            } catch (NumberFormatException e) {
                Notification.show("Invalid blip ID");
                return;
            }
            Notification.show(String.valueOf(id));

            Optional<Submission> submission = submissionProxy.getSubmission(id);
            submission.ifPresentOrElse( s -> {
                        if (s instanceof PollutionReport pollutionReport) {
                            Notification.show("Opening pollution report " + id);
                            openSubmissionWindow(pollutionReport);
                        } else if (s instanceof SpeciesSpotting speciesSpotting) {
                            Notification.show("Opening species spotting " + id);
                            openSubmissionWindow(speciesSpotting);
                        }
                    },
                    () -> {
                        Notification.show("Unknown submission type");
                    }
            );
        }
    }

    private Dialog submissionDialog() {
        final Dialog dialog = new Dialog();
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING); // optional
        dialog.setHeaderTitle("Person details");
        dialog.setModal(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("520px");
        dialog.setMaxWidth("90vw");
        dialog.getElement().setAttribute("aria-label", "Person details");

        return dialog;
    }

    private void openSubmissionWindow(PollutionReport pollutionReport) {
        final Dialog dialog = submissionDialog();
        dialog.add(new PollutionReportView(pollutionReport));

        dialog.open();
    }

    private void openSubmissionWindow(SpeciesSpotting speciesSpotting) {
        final Dialog dialog = submissionDialog();
        dialog.add(new SpeciesSubmissionView(speciesSpotting));

        dialog.open();
    }
}
