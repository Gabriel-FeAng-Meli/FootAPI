package com.meli.footapi.repository;

import com.meli.footapi.entity.Stadium;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface StadiumRepo extends JpaRepository<Stadium, Integer> {

}
