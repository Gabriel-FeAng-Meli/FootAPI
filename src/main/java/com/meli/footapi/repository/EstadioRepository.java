package com.meli.footapi.repository;

import com.meli.footapi.entity.Estadio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface EstadioRepository extends JpaRepository<Estadio, Integer> {
    Page<Estadio> findByNome(String nome, Pageable pageable);
}
