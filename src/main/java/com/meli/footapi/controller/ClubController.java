package com.meli.footapi.controller;


import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.service.ClubeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
public class ClubController {

    @Autowired
    private ClubeService clubService;

    @PostMapping
    public ResponseEntity<ClubeDto> createClub(@RequestBody Clube club) {
        return ResponseEntity.status(201).body(clubService.createClub(club));
    }

    @GetMapping("/{id}")
    public ClubeDto getClubById(@PathVariable(value = "id") int clubId) {
        return clubService.getClubById(clubId);
    }

    @GetMapping
    public List<ClubeDto> getAllClubs() {
        return clubService.getClubs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(@PathVariable(value = "id") int clubId, @RequestBody Clube newClubInfo) {
        return ResponseEntity.status(204).body(clubService.updateClub(clubId, newClubInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable(value = "id") int clubId) {
        clubService.deleteClub(clubId);

        return ResponseEntity.noContent().build();
    }
}
