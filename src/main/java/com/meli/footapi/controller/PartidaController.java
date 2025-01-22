package com.meli.footapi.controller;


import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.PartidaService;

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
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaDto> createPartida(@RequestBody Partida match) {
        return ResponseEntity.status(201).body(partidaService.createPartida(match));
    }

    @GetMapping("/{id}")
    public PartidaDto getPartidaById(@PathVariable(value = "id") int matchId) {
        return partidaService.getMatchById(matchId);
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getPartidasPaginadas(
        @RequestParam(required = false) Boolean isGoleada, 
        @RequestParam(required = false) String clubeDaCasa,
        @RequestParam(required = false) String clubeVisitante,
        @RequestParam(defaultValue = "0") int pagina, 
        @RequestParam(defaultValue = "5") int limite) {

        return ResponseEntity.ok(partidaService.paginarPartidas(isGoleada, clubeDaCasa, clubeVisitante, pagina, limite));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable(value = "id") int matchId, @RequestBody Partida informaçãoAtualizada) {
        return ResponseEntity.status(200).body(partidaService.updateMatch(matchId, informaçãoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable(value = "id") int matchId) {
        partidaService.deleteMatch(matchId);

        return ResponseEntity.noContent().build();
    }
}
