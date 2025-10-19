package com.tsimerekis.submission;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("SPECIES")
public class SpeciesSpotting extends Submission {

    @OneToOne(optional = true)
    @JoinColumn(name = "species_species_id")
    private Species species;

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

}
