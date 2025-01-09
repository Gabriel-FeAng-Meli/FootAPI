package com.meli.footapi.controller;


import com.meli.footapi.dto.MatchDto;
import com.meli.footapi.service.MatchService;

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
    private MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchDto> createMatch(@RequestBody MatchDto match) {
        return ResponseEntity.status(201).body(matchService.createMatch(match));
    }

    @GetMapping("/{id}")
    public MatchDto getMatchById(@PathVariable(value = "id") int matchId) {
        return matchService.getMatchById(matchId);
    }

    @GetMapping
    public List<MatchDto> getAllMatchs() {
        return matchService.getMatchs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable(value = "id") int matchId, @RequestBody MatchDto newMatchInfo) {
        return ResponseEntity.status(204).body(matchService.updateMatch(matchId, newMatchInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable(value = "id") int matchId) {
        matchService.deleteMatch(matchId);

        return ResponseEntity.noContent().build();
    }
}
