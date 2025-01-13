package com.meli.footapi.controller;


import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.service.EstadioService;

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
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService stadiumService;

    @PostMapping
    public ResponseEntity<EstadioDto> createStadium(@RequestBody Estadio stadium) {
        return ResponseEntity.status(201).body(stadiumService.createStadium(stadium));
    }

    @GetMapping("/{id}")
    public EstadioDto getStadiumById(@PathVariable(value = "id") int stadiumId) {
        return stadiumService.getStadiumById(stadiumId);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStadiums(        
        @RequestParam(required = false) String nome,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

            try {
            List<Estadio> estadios = new ArrayList<Estadio>();
            Page<Estadio> paginaEstadio;
            if(nome == null)
                paginaEstadio = stadiumService.findAll(size, page);
            else {
                paginaEstadio = stadiumService.findByNome(nome, size, page);
            }

            estadios = paginaEstadio.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Estadios", estadios);
            response.put("paginaAtual", paginaEstadio.getNumber() + 1);
            response.put("totalDeItens", paginaEstadio.getTotalElements());
            response.put("totalDePaginas", paginaEstadio.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel realizar a busca com os parametros fornecidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStadium(@PathVariable(value = "id") int stadiumId, @RequestBody Estadio newStadiumInfo) {
        return ResponseEntity.status(204).body(stadiumService.updateStadium(stadiumId, newStadiumInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable(value = "id") int stadiumId) {
        stadiumService.deleteStadium(stadiumId);

        return ResponseEntity.noContent().build();
    }
}
