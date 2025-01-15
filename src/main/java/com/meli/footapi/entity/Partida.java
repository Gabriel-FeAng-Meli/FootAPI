package com.meli.footapi.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataPartida;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false, name = "estadio_id")
    private Estadio estadio;

    private boolean goleada = golsClubeDaCasa - golsClubeVisitante >= 3 || golsClubeVisitante - golsClubeDaCasa >= 3;

}
