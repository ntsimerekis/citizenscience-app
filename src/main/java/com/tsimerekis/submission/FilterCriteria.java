package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.SubmissionType;

import java.time.LocalDateTime;

public class FilterCriteria {
    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public SubmissionType submissionType;

    public Double minPM25;
    public Double maxPM25;

    public Double minPM10;
    public Double maxPM10;

    public Double minTemperature;
    public Double maxTemperature;
}
