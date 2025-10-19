package com.tsimerekis.submission;

import jakarta.persistence.*;

@Entity
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="species_id")
    private Long id;

    private String speciesName;
}
