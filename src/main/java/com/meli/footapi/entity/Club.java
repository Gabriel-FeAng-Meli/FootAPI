package com.meli.footapi.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clubes")
public class Club {

    @Id
    @GeneratedValue
    @Column(nullable=false, name="clube_id")
    private int id;
    
    @Column(name="clube_nome")
    private String name;

    @Column(name="clube_estado")
    private String state;

    @Column(name="clube_ativo")
    private boolean active;

    @Column(name="clube_data")
    private LocalDate date;

}

