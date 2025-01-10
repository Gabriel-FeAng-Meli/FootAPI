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
    
    private String nome;
    
    private String estado;
    
    private boolean ativo;

    private LocalDate dataDeCriacao;

    @OneToMany
    private List<Partida> partidasDeCasa;

    @OneToMany
    private List<Partida> partidasComoVisitante;

    @OneToOne(mappedBy = "clube")
    private Estadio estadio;

}

