package com.tsimerekis.submission.repository;

import com.tsimerekis.submission.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpeciesRepository extends JpaRepository<Species, Long> {

    @Query(value = """
        SELECT *
        FROM species
        WHERE similarity(species_name, :speciesName) > 0.3
        ORDER BY similarity(species_name, :speciesName) DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Species> fuzzySearchSpeciesName(String speciesName);
}
