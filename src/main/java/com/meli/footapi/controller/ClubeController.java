package com.meli.footapi.controller;


import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.service.ClubeService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping
    public ResponseEntity<ClubeDto> createClub(@RequestBody Clube club) {
        return ResponseEntity.status(201).body(clubeService.createClub(club));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeDto> getClubById(@PathVariable(value = "id") int clubId) {
        return ResponseEntity.status(200).body(clubeService.getClubeById(clubId));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPaginatedClubes(
        @RequestParam(required = false) String nome,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(clubeService.getPaginatedClubs(nome, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(@PathVariable(value = "id") int clubId, @RequestBody Clube newClubInfo) {
        return ResponseEntity.status(204).body(clubeService.updateClube(clubId, newClubInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable(value = "id") int clubId) {
        clubeService.deleteClube(clubId);

        return ResponseEntity.noContent().build();
    }
}
