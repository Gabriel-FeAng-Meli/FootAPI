package com.meli.footapi.controller;


import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.PartidaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

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
    public ResponseEntity<Map<String, Object>> getAllMatchs(
        @RequestParam(required = false) Boolean goleada, 
        @RequestParam(required = false) String clubeDaCasa,
        @RequestParam(required = false) String clubeVisitante,
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size) {
        try {
            List<Partida> partidas = new ArrayList<Partida>();
            Page<Partida> paginaPartida;
            if(goleada != null)
                paginaPartida = matchService.getPagedPartidaByGoleada(goleada, page, size);
            else if(clubeDaCasa != null)
                paginaPartida = matchService.getPagedByNomeClubeDaCasa(clubeDaCasa, page, size);
            else if(clubeVisitante != null)
                paginaPartida = matchService.getPagedByNomeClubeVisitante(clubeVisitante, page, size);
            else {
                paginaPartida = matchService.getPagedPartidas(page, size);                
            }

            partidas = paginaPartida.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Partidas", partidas);
            response.put("paginaAtual", paginaPartida.getNumber() + 1);
            response.put("totalDeItens", paginaPartida.getTotalElements());
            response.put("totalDePaginas", paginaPartida.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel realizar a busca com os parametros fornecidos");
        }
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
