package com.tsimerekis.submission.species;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpeciesRepositoryTest {
    @Autowired
    SpeciesRepository speciesRepository;

    @Test
    void getSpecies() {

    }

    @Test
    void addSpecies() {
        final Species species = new Species();

        Species species_saved= speciesRepository.save(species);

        assertNotNull(species_saved);
    }

    @Test
    void fuzzySearchSpeciesName() {
    }
}