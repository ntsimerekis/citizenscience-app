package com.tsimerekis.submission.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(SubmissionType.POLLUTION)
public class PollutionReport extends Submission {

    private Double pm25;

    private Double pm10;

    private Double durationHours;       // measurement period in minutes

    private String sensorType;    // e.g., "AirVisual Pro"

    @Override
    public boolean isValid() {
        return super.isValid() && pm25 != null && pm10 != null;
    }

    public Double getPM25() { return pm25; }
    public void setPM25(Double pm25) { this.pm25 = pm25; }

    public Double getPM10() { return pm10; }
    public void setPM10(Double pm10) { this.pm10 = pm10; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double hours) { this.durationHours = hours; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }
}