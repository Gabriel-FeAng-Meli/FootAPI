package com.meli.footapi.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CLUB")
public class Club {

    @GeneratedValue
    @Id
    @Column(nullable=false, name="ID")
    private int id;
    
    @Column(name="NAME")
    private String name;

    @Column(name="STATE")
    private String state;

    @Column(name="ACTIVE")
    private boolean active;

    @Column(name="DATE")
    private LocalDate date;

}

