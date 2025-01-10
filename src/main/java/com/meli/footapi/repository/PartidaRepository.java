package com.meli.footapi.repository;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PartidaRepository extends JpaRepository<Partida, Integer> {

    @Query("select p from partidas p where p.clube_da_casa_id=:clubeDaCasa.id order by data_partida desc")
    public List<Partida> findByClubeDaCasa(Clube clubeDaCasa);

    @Query("select p from partidas p where p.clube_visitante_id=:clubeVisitante.id order by data_partida")
    public List<Partida> findByClubeVisitante(Clube clubeVisitante);

    @Query("select p from partidas p where p.estadio_id=:estadio.id order by p.data_partida desc")
    public List<Partida> findByEstadio(Estadio estadio);
}
