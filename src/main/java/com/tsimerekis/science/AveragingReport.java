package com.tsimerekis.science;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;

import java.util.ArrayList;
import java.util.List;

public class AveragingReport {
    private List<Submission> orderedSubmissions;

    private boolean speciesSubmissionPresent;

    private boolean pollutionReportPresent;

    private Double averagePM10;

    private Double averagePM25;

    private Double averageTemperature;

    private Double averageAltitude;

    public List<Submission> getOrderedSubmissions() {
        return new ArrayList<Submission>(orderedSubmissions);
    }

    public void addReport(Submission submission) {

    }

    public void addReport(SpeciesSpotting spotting) {

    }

    public void addReport(PollutionReport pollutionReport) {

    }

    public boolean speciesSubmissionPresent() {
        return speciesSubmissionPresent;
    }

    public boolean pollutionReportPresent() {
        return pollutionReportPresent;
    }

    public Double getAveragePM10() {
        return averagePM10;
    }

    public Double getAveragePM25() {
        return averagePM25;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public Double getAverageAltitude() {
        return averageAltitude;
    }
}
