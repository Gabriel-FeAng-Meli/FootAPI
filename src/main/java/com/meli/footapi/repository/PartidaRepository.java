package com.meli.footapi.repository;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PartidaRepository extends JpaRepository<Partida, Integer>{

    public List<Partida> findByClubeDaCasa(Clube clubeDaCasa);

    public Partida findById(int id);

    public List<Partida> findByClubeVisitante(Clube clubeVisitante);

    Page<Partida> findByClubeDaCasaNomeContains(String nome, Pageable pageable);
    
    Page<Partida> findByClubeVisitanteNomeContains(String nome, Pageable pageable);

    public List<Partida> findByEstadio(Estadio estadio);

    Page<Partida> findByGoleada(boolean goleada, Pageable pageable);

}
