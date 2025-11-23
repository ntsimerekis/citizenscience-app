package com.tsimerekis.science;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;

import java.util.ArrayList;
import java.util.List;

public class AveragingReport {
    private final List<Submission> orderedSubmissions = new ArrayList<>();

    private boolean speciesSubmissionPresent;
    private boolean pollutionReportPresent;

    private Double averagePM10;
    private Double averagePM25;
    private Double averageTemperature;
    private Double averageAltitude;

    public List<Submission> getOrderedSubmissions() {
        return new ArrayList<>(orderedSubmissions);
    }

    /** Add a single submission (any type). */
    public void addReport(Submission submission) {
        if (submission == null) return;
        orderedSubmissions.add(submission);

        if (submission instanceof PollutionReport) {
            pollutionReportPresent = true;
        } else if (submission instanceof SpeciesSpotting) {
            speciesSubmissionPresent = true;
        }

        recalculateAverages();
    }

    /** Add a list of submissions at once. */
    public void addReports(List<? extends Submission> submissions) {
        if (submissions == null || submissions.isEmpty()) return;

        for (Submission submission : submissions) {
            if (submission instanceof PollutionReport) {
                pollutionReportPresent = true;
            } else if (submission instanceof SpeciesSpotting) {
                speciesSubmissionPresent = true;
            }
        }

        orderedSubmissions.addAll(submissions);
        recalculateAverages();
    }

    private void recalculateAverages() {
        double totalPM10 = 0;
        double totalPM25 = 0;
        double totalTemp = 0;
        double totalAlt = 0;

        int pmCount = 0;
        int pm25Count = 0;
        int tempCount = 0;
        int altCount = 0;

        for (Submission submission : orderedSubmissions) {
            if (submission instanceof PollutionReport pr) {
                if (pr.getPm10() != null) {
                    totalPM10 += pr.getPm10();
                    pmCount++;
                }
                if (pr.getPm25() != null) {
                    totalPM25 += pr.getPm25();
                    pm25Count++;
                }
                if (pr.getTemperatureCelsius() != null) {
                    totalTemp += pr.getTemperatureCelsius();
                    tempCount++;
                }
                if (pr.getAltitudeMeters() != null) {
                    totalAlt += pr.getAltitudeMeters();
                    altCount++;
                }
            } else if (submission instanceof SpeciesSpotting ss) {
                if (ss.getAltitudeMeters() != null) {
                    totalAlt += ss.getAltitudeMeters();
                    altCount++;
                }
            }
        }

        averagePM10 = pmCount > 0 ? totalPM10 / pmCount : null;
        averagePM25 = pm25Count > 0 ? totalPM25 / pm25Count : null;
        averageTemperature = tempCount > 0 ? totalTemp / tempCount : null;
        averageAltitude = altCount > 0 ? totalAlt / altCount : null;
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