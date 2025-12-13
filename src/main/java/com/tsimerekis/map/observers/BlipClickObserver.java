package com.tsimerekis.map.observers;

import com.tsimerekis.map.Blip;
import com.tsimerekis.map.averaging.event.RowClickEvent;
import com.tsimerekis.map.proxy.SubmissionProxy;
import com.tsimerekis.map.ui.DialogHelper;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.ui.PollutionReportView;
import com.tsimerekis.submission.ui.SpeciesSubmissionView;
import com.tsimerekis.submission.ui.SubmissionViewFactory;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.map.events.MapFeatureClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
//@Scope("prototype")
@UIScope
public class BlipClickObserver {

    private final SubmissionViewFactory submissionViewFactory;

    private final SubmissionProxy submissionProxy;

    public BlipClickObserver(@Autowired SubmissionProxy submissionProxy,
                             @Autowired SubmissionViewFactory submissionViewFactory) {
        this.submissionProxy = submissionProxy;
        this.submissionViewFactory = submissionViewFactory;
    }

    @EventListener
    public void onRowClick(RowClickEvent rowClickEvent) {
        openSubmission(rowClickEvent.submission());
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
                        openSubmission(s);
                    },
                    () -> {
                        Notification.show("Unknown submission type");
                    }
            );
        }
    }

    private void openSubmission(Submission submission) {
        final Dialog dialog = DialogHelper.submissionDialog();
        dialog.add(submissionViewFactory.createSubmissionView(submission));
        dialog.open();
    }
}
