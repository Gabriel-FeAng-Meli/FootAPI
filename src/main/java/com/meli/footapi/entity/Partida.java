package com.meli.footapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false, name = "clube_da_casa_id")
    private Clube clubeDaCasa;

    @Column(nullable = false, updatable = false)
    private int golsClubeDaCasa;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false, name = "clube_visitante_id")
    private Clube clubeVisitante;

    @Column(nullable = false, updatable = false)
    private int golsClubeVisitante;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false, name = "estadio_id")
    private Estadio estadio;

}
