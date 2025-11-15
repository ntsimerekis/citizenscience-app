package com.tsimerekis.submission;

import com.tsimerekis.submission.entity.SubmissionType;

import java.time.LocalDateTime;

public class FilterCriteria {
    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public SubmissionType submissionType;

    public Float minPM25;
    public Float maxPM25;

    public Float minPM10;
    public Float maxPM10;

//    public Float minPressure;
//    public Float maxPressure;

    public Float minTemperature;
    public Float maxTemperature;
}
