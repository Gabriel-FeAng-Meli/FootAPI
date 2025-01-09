package com.meli.footapi.controller;


import com.meli.footapi.dto.StadiumDto;
import com.meli.footapi.service.StadiumService;

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
@RequestMapping("/estadios")
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumDto> createStadium(@RequestBody StadiumDto stadium) {
        return ResponseEntity.status(201).body(stadiumService.createStadium(stadium));
    }

    @GetMapping("/{id}")
    public StadiumDto getStadiumById(@PathVariable(value = "id") int stadiumId) {
        return stadiumService.getStadiumById(stadiumId);
    }

    @GetMapping
    public List<StadiumDto> getAllStadiums() {
        return stadiumService.getStadiums();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStadium(@PathVariable(value = "id") int stadiumId, @RequestBody StadiumDto newStadiumInfo) {
        return ResponseEntity.status(204).body(stadiumService.updateStadium(stadiumId, newStadiumInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable(value = "id") int stadiumId) {
        stadiumService.deleteStadium(stadiumId);

        return ResponseEntity.noContent().build();
    }
}
