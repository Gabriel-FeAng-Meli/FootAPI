package com.meli.footapi.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "estadios")
public class Estadio {

    @Id
    @GeneratedValue
    private int id;
    
    private String name;

    @OneToMany
    private List<Partida> partida;

    @OneToOne
    @JoinColumn(name = "estadio_id")
    private Clube clube;
}
