package com.tsimerekis.submission.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // or JOINED
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class Submission {

    @Id
    @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected LocalDateTime observedAt;

    protected Point location;

    private String note;

    private Double temperatureCelsius;    // °C

    private Double altitudeMeters;       // meters

    @ElementCollection
    @CollectionTable(name = "submission_comments")
    private List<String> comments = new ArrayList<String>();

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(LocalDateTime observedAt) { this.observedAt = observedAt; }

    public Point getLocation() { return location; }

    public void setLocation(Point location) { this.location = location; }

//    public boolean isValid() {
//        return location != null && observedAt != null;
//    }

    public boolean isValid() {
            return true;
    }

    public Optional<Long> getSubmissionId() { return Optional.of(id); }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Double temperature) { this.temperatureCelsius = temperature; }

    public Double getAltitudeMeters() { return altitudeMeters; }
    public void setAltitudeMeters(Double altitude) { this.altitudeMeters = altitude; }
}
