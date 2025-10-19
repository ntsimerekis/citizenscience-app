package com.tsimerekis.submission;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.Type;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("POLLUTION")
public class PollutionReport extends Submission {

    private Float pm25;

    private Float pm10;

    private Float duration;       // measurement period in minutes

    private String sensorType;    // e.g., "AirVisual Pro"

    private Float temperature;    // °C

    private Float altitude;       // meters

    // ----- Utility methods -----
    @Override
    public boolean isValid() {
        return super.isValid() && pm25 != null && pm10 != null;
    }

    public float averagePm() {
        if (pm25 == null || pm10 == null) return 0f;
        return (pm25 + pm10) / 2f;
    }

    // ----- Getters / Setters -----
//    public Long getReportId() { return reportId; }
//    public void setReportId(Long reportId) { this.reportId = reportId; }

//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }

    public Float getPm25() { return pm25; }
    public void setPm25(Float pm25) { this.pm25 = pm25; }

    public Float getPm10() { return pm10; }
    public void setPm10(Float pm10) { this.pm10 = pm10; }

    public Float getDuration() { return duration; }
    public void setDuration(Float duration) { this.duration = duration; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }

    public Float getTemperature() { return temperature; }
    public void setTemperature(Float temperature) { this.temperature = temperature; }

    public Float getAltitude() { return altitude; }
    public void setAltitude(Float altitude) { this.altitude = altitude; }
}