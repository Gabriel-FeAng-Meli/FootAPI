package com.meli.footapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "estadio")
public class Stadium {

    @Id
    @GeneratedValue
    @Column(name="estadio_id")
    private int id;
    
    @Column(name="estadio_nome")
    private String name;

}
