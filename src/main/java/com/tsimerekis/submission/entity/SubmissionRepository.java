package com.tsimerekis.submission.entity;

import com.tsimerekis.submission.FilterCriteria;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>, JpaSpecificationExecutor<Submission> {

//    // common queries
    @Query("""
    select s from Submission s
    where s.observedAt between :start and :end
      and st_intersects(s.location, :bbox) = true
  """)
    List<Submission> findAllInRangeAndBbox(Instant start, Instant end, Geometry bbox);

    //
//    // subclass-specific queries using the subclass entity in JPQL:
//    @Query("select sp from SpeciesSpotting sp where sp.species.id = :taxonId")
//    List<SpeciesSpotting> findSpeciesByTaxonId(Long taxonId);
//
//    @Query("select pr from PollutionReport pr where pr.pm25 between :min and :max")
//    List<PollutionReport> findPollutionByPm25(float min, float max);
}