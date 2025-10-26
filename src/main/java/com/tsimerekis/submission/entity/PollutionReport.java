package com.tsimerekis.submission.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(SubmissionType.POLLUTION)
public class PollutionReport extends Submission {

    private Float pm25;

    private Float pm10;

    private Float durationHours;       // measurement period in minutes

    private String sensorType;    // e.g., "AirVisual Pro"

    @Override
    public boolean isValid() {
        return super.isValid() && pm25 != null && pm10 != null;
    }

    public Float getPM25() { return pm25; }
    public void setPM25(Float pm25) { this.pm25 = pm25; }

    public Float getPM10() { return pm10; }
    public void setPM10(Float pm10) { this.pm10 = pm10; }

    public Float getDurationHours() { return durationHours; }
    public void setDurationHours(Float hours) { this.durationHours = hours; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }
}