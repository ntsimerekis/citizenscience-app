package com.tsimerekis.submission.ui;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SubmissionViewFactory {

    public static VerticalLayout createSubmissionView(Submission submission) {
        if (submission instanceof PollutionReport pollutionReport) {
            return new PollutionReportView(pollutionReport);
        } else if (submission instanceof SpeciesSpotting spotting) {
            return new SpeciesSubmissionView(spotting);
        }

        return null;
    }

    public static VerticalLayout createSubmissionForm(Submission submission) {
        if (submission instanceof PollutionReport pollutionReport) {
            final PollutionReportView pollutionReportView = new PollutionReportView(pollutionReport);
            pollutionReportView.allowWrite();
            return pollutionReportView;
        } else if (submission instanceof SpeciesSpotting spotting) {
            final SpeciesSubmissionView speciesSubmissionView = new SpeciesSubmissionView(spotting);
            speciesSubmissionView.allowWrite();
            return speciesSubmissionView;
        }

        return null;
    }

}
