package com.tsimerekis.submission.species;

import java.util.List;

import com.tsimerekis.submission.entity.Species;
import com.tsimerekis.submission.repository.SpeciesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpeciesRepositoryTest {
    @Autowired
    SpeciesRepository speciesRepository;

    @Test
    void saveSpeciesNotNull() {
        final Species species = new Species();
        species.setSpeciesName("homo erectus");

        Species species_saved = speciesRepository.save(species);

        assertNotNull(species_saved);
    }

    /*
      SQL will contain "homo sapien"
     */
    @Test
    void fuzzySearchSpeciesName() {
        List<Species> species = speciesRepository.fuzzySearchSpeciesName("homo sapeen");

        assertEquals("homo sapien", species.getFirst().getSpeciesName());
    }
}