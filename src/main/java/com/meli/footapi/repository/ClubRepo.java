package com.meli.footapi.repository;

import com.meli.footapi.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ClubRepo extends JpaRepository<Club, Integer> {

}
