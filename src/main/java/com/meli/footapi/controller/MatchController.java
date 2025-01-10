package com.meli.footapi.controller;


import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.PartidaService;

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
@RequestMapping("/partidas")
public class MatchController {

    @Autowired
    private PartidaService matchService;

    @PostMapping
    public ResponseEntity<PartidaDto> createMatch(@RequestBody Partida match) {
        return ResponseEntity.status(201).body(matchService.createMatch(match));
    }

    @GetMapping("/{id}")
    public PartidaDto getMatchById(@PathVariable(value = "id") int matchId) {
        return matchService.getMatchById(matchId);
    }

    @GetMapping
    public List<PartidaDto> getAllMatchs() {
        return matchService.getMatchs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable(value = "id") int matchId, @RequestBody Partida informaçãoAtualizada) {
        return ResponseEntity.status(204).body(matchService.updateMatch(matchId, informaçãoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable(value = "id") int matchId) {
        matchService.deleteMatch(matchId);

        return ResponseEntity.noContent().build();
    }
}
