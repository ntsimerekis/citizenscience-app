package com.tsimerekis.submission.repository;

import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.entity.SubmissionType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;

public class SubmissionSpecs {

    public static Specification<Submission> forPollutionReport(FilterCriteria c) {
        return (root, query, cb) -> {
            // treat must be called on a From; cast root accordingly
//            From<?, ?> from = (From<?, ?>) root;
//            From<?, ?> pollutionRoot = from.treat(PollutionReport.class);
            Root<PollutionReport> pollutionRoot = cb.treat(root, PollutionReport.class);

            var predicates = new ArrayList<Predicate>();

            // ensure the row is actually the subclass type
            predicates.add(cb.equal(pollutionRoot.type(), PollutionReport.class));

            Expression<Double> pm25 = pollutionRoot.get("pm25");
            if (c.minPM25 != null && c.maxPM25 != null) {
                predicates.add(cb.or(cb.isNull(pm25), cb.between(pm25, c.minPM25, c.maxPM25)));
            } else if (c.minPM25 != null) {
                predicates.add(cb.or(cb.isNull(pm25), cb.ge(pm25, c.minPM25)));
            } else if (c.maxPM25 != null) {
                predicates.add(cb.or(cb.isNull(pm25), cb.le(pm25, c.maxPM25)));
            }

            Expression<Double> pm10 = pollutionRoot.get("pm10");
            if (c.minPM10 != null && c.maxPM10 != null) {
                predicates.add(cb.or(cb.isNull(pm10), cb.between(pm10, c.minPM10, c.maxPM10)));
            } else if (c.minPM10 != null) {
                predicates.add(cb.or(cb.isNull(pm10), cb.ge(pm10, c.minPM10)));
            } else if (c.maxPM10 != null) {
                predicates.add(cb.or(cb.isNull(pm10), cb.le(pm10, c.maxPM10)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public static Specification<Submission> byFilterCriteria(FilterCriteria c) {
        return (root, query, cb) -> {
            final Root<?> realRoot;

            if (SubmissionType.SPECIES.equals(c.submissionType)) {
                realRoot = cb.treat(root, SpeciesSpotting.class);
            } else if (SubmissionType.POLLUTION.equals(c.submissionType)) {
                realRoot = cb.treat(root, PollutionReport.class);
            } else {
                realRoot = root;
            }

            var predicates = new ArrayList<Predicate>();
            if (c.minTemperature != null && c.maxTemperature != null) {
                predicates.add(cb.between(realRoot.get("temperatureCelsius"), c.minTemperature, c.maxTemperature));
            }
            else if (c.minTemperature != null)
                predicates.add(cb.ge(realRoot.get("temperatureCelsius"), c.minTemperature));
            else if (c.maxTemperature != null)
                predicates.add(cb.le(realRoot.get("temperatureCelsius"), c.maxTemperature));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

     public static Specification<Submission> intersects(Geometry bbox) {
        return (root, query, cb) -> {
            // Hibernate 6 + PostGIS: call the SQL function directly
            Expression<Boolean> fn = cb.function(
                    "st_intersects",
                    Boolean.class,
                    root.get("location"),
                    cb.literal(bbox)   // use literal so we don't need to bind a Criteria parameter
            );
            return cb.isTrue(fn);
        };
    }

    public static Specification<Submission> observedBetween(Instant start, Instant end) {
        return (root, query, cb) -> cb.between(root.get("observedAt"), start, end);
    }
}
