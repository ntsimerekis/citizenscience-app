package com.tsimerekis.submission.repository;

import java.time.Instant;

public class FilterCriteria {
    public Instant lowestObservedDate = Instant.EPOCH;
    public Instant highestObservedDate = Instant.now();

    public String submissionType;

    public Double minPM25 = 0.0;
    public Double maxPM25 = 100.0;

    public Double minPM10 = 0.0;
    public Double maxPM10 = 250.0;

    public Double minTemperature = -30.0;
    public Double maxTemperature = 40.0;

    public Instant getLowestObservedDate() {
        return lowestObservedDate;
    }

    public void setLowestObservedDate(Instant lowestObservedDate) {
        this.lowestObservedDate = lowestObservedDate;
    }

    public Instant getHighestObservedDate() {
        return highestObservedDate;
    }

    public void setHighestObservedDate(Instant highestObservedDate) {
        this.highestObservedDate = highestObservedDate;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }

    public Double getMinPM25() {
        return minPM25;
    }

    public void setMinPM25(Double minPM25) {
        this.minPM25 = minPM25;
    }

    public Double getMaxPM25() {
        return maxPM25;
    }

    public void setMaxPM25(Double maxPM25) {
        this.maxPM25 = maxPM25;
    }

    public Double getMinPM10() {
        return minPM10;
    }

    public void setMinPM10(Double minPM10) {
        this.minPM10 = minPM10;
    }

    public Double getMaxPM10() {
        return maxPM10;
    }

    public void setMaxPM10(Double maxPM10) {
        this.maxPM10 = maxPM10;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
