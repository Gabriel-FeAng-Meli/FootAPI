package com.meli.footapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "partidas")
public class Match {

    @Id
    @GeneratedValue
    @Column(name = "partida_id")
    private int id;

    @Column(name = "partida_id_clube_casa")
    private int homeClubId;

}
