package com.meli.footapi.repository;

import com.meli.footapi.entity.Clube;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ClubeRepository extends JpaRepository<Clube, Integer> {
}
