package com.meli.footapi.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "clubes")
public class Clube {

    @Id
    @GeneratedValue
    @Column(nullable=false)
    private int id;
    
    private String name;
    
    private String state;
    
    private boolean active;

    private LocalDate date;

    @OneToMany
    private List<Partida> partidaDeCasa;

    @OneToMany
    private List<Partida> partidaVisitante;

    @OneToOne(mappedBy = "clube")
    private Estadio estadio;

}

