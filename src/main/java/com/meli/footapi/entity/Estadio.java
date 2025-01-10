package com.meli.footapi.entity;

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
    
    private String nome;

    @OneToOne
    @JoinColumn(name = "estadio_id")
    private Clube clube;

}
