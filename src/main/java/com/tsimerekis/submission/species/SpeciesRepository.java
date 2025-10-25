package com.tsimerekis.submission.species;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpeciesRepository extends JpaRepository<Species, Long> {

    @Query(value = """
        SELECT *
        FROM species
        WHERE similarity(speciesName, :q) > 0.3
        ORDER BY similarity(speciesName, :q) DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Species> fuzzySearchSpeciesName(String speciesname);

}
