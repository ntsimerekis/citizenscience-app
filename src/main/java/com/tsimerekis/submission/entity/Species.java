package com.tsimerekis.submission.entity;

import jakarta.persistence.*;

@Entity
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="species_id")
    private Long id;

    //always should be lowercase
    private String speciesName;

    public Species() {

    }

    public Species(String speciesName) {
        setSpeciesName(speciesName);
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName.toLowerCase();
    }

    public String getSpeciesName() {
        return speciesName;
    }
}
