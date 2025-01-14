package com.meli.footapi.controller;


import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.service.EstadioService;

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
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    public ResponseEntity<EstadioDto> createEstadio(@RequestBody Estadio estadio) {
        return ResponseEntity.status(201).body(estadioService.createStadium(estadio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDto> getEstadioById(@PathVariable(value = "id") int stadiumId) {
        return ResponseEntity.ok(estadioService.getStadiumById(stadiumId));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEstadiosPaginados(        
        @RequestParam(required = false) String nome,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

         return ResponseEntity.ok(estadioService.paginarEstadios(nome, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStadium(@PathVariable(value = "id") int stadiumId, @RequestBody Estadio newStadiumInfo) {
        return ResponseEntity.status(204).body(estadioService.updateStadium(stadiumId, newStadiumInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable(value = "id") int stadiumId) {
        estadioService.deleteStadium(stadiumId);

        return ResponseEntity.noContent().build();
    }
}
