package com.tsimerekis.map.averaging;

import com.tsimerekis.science.AveragingReport;

public class AveragingDoneEvent {

    private final AveragingReport report;

    public AveragingDoneEvent(AveragingReport report) {
        this.report = report;
    }

    public AveragingReport getReport() {
        return report;
    }
}
