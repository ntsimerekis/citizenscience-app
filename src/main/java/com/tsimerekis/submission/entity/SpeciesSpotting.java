package com.tsimerekis.submission.entity;

import com.tsimerekis.submission.species.Species;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue(SubmissionType.SPECIES)
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
