package com.tsimerekis.science;

import com.tsimerekis.submission.entity.Submission;

import java.util.ArrayList;
import java.util.List;

public class AveragingReport {
    private List<Submission> orderedSubmissions;

    private boolean speciesSubmissionPresent;

    private boolean pollutionReportPresent;

    private Float averagePM10;

    private Float averagePM25;

    private Float averageTemperature;

    private Float averageAltitude;

    public List<Submission> getOrderedSubmissions() {
        return new ArrayList<Submission>(orderedSubmissions);
    }

    public void addReport(Submission submission) {
        orderedSubmissions.add(submission);
    }

    public boolean speciesSubmissionPresent() {
        return speciesSubmissionPresent;
    }

    public boolean pollutionReportPresent() {
        return pollutionReportPresent;
    }

    public Float getAveragePM10() {
        return averagePM10;
    }

    public void setAveragePM10(Float averagePM10) {
        this.averagePM10 = averagePM10;
    }

    public Float getAveragePM25() {
        return averagePM25;
    }

    public void setAveragePM25(Float averagePM25) {
        this.averagePM25 = averagePM25;
    }

    public Float getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Float averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Float getAverageAltitude() {
        return averageAltitude;
    }

    public void setAverageAltitude(Float averageAltitude) {
        this.averageAltitude = averageAltitude;
    }
}
