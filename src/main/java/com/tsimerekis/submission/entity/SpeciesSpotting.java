package com.tsimerekis.submission.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(SubmissionType.SPECIES)
public class SpeciesSpotting extends Submission {

    @ManyToOne(optional = true)
    @JoinColumn(name = "species_species_id")
    private Species species;

    @Override
    public boolean isValid() {
        return super.isValid() && species != null && species.getSpeciesName() != null && !species.getSpeciesName().isEmpty();
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
}
