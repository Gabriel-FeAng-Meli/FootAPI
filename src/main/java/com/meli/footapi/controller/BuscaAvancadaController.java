package com.meli.footapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.service.BuscaAvancadaService;

@RestController
@RequestMapping("/busca")
public class BuscaAvancadaController {

    @Autowired
    private BuscaAvancadaService buscaAvancadaService;

    @GetMapping("/{id}/retrospectiva")
    public ResponseEntity<RetrospectivaDto> getRetrospectiva(@PathVariable(value = "id") int clubId) {
        return ResponseEntity.status(200).body(buscaAvancadaService.getRetrospectiva(clubId));
    }

    @GetMapping("/{id1}/retrospectiva/{id2}")
    public ResponseEntity<RetrospectivaDto> getConfrontosDiretos(@PathVariable(value = "id1") int clubId, @PathVariable(value = "id2") int otherClubId) {
        return ResponseEntity.status(200).body(buscaAvancadaService.getConfrontosDiretos(clubId, otherClubId));
    }

    @GetMapping("/{id}/retrospectiva/clubes")
    public ResponseEntity<List<RetrospectivaDto>> getRetrospectivaParaCadaAdversario(@PathVariable(value = "id") int clubId) {
        return ResponseEntity.ok(buscaAvancadaService.getRetrospectivaParaCadaAdversario(clubId));
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDto>> getRanking() {
        return ResponseEntity.ok(buscaAvancadaService.getRanking());
    }
}
