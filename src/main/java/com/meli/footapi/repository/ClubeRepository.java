package com.meli.footapi.repository;

import com.meli.footapi.entity.Clube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
@EnableJpaRepositories
public interface ClubeRepository extends JpaRepository<Clube, Integer> {

    List<Clube> findByAtivo(boolean ativo);

    Page<Clube> findByAtivo(boolean ativo, Pageable pageable);

    Page<Clube> findByNomeContaining(String nomeDoClube, Pageable pageable);

}
