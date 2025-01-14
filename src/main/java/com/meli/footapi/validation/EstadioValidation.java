package com.meli.footapi.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.repository.EstadioRepository;

@Service
public class EstadioValidation {

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    EstadioRepository estadioRepository;

    public void validateStadiumInput(Estadio estadioParaValidar) {

        Clube clubeAssociado = estadioParaValidar.getClube();
        validateHomeClub(clubeAssociado);

        String inputedName = estadioParaValidar.getNome();
        validateStadiumName(inputedName);

        List<Estadio> estadiosExistentes = estadioRepository.findAll();

        estadioRepository.findByNome(inputedName, null);
        checkIfStadiumAlreadyExists(inputedName, estadiosExistentes);
    }

    private void checkIfStadiumAlreadyExists(String inputedName, List<Estadio> estadiosExistentes) {
        estadiosExistentes.stream().forEach(estadio -> {
            if(estadio.getNome().equals(inputedName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um estadio de mesmo nome já está cadastrado.");
            }
        });
    }

    private void validateStadiumName(String inputedName) {
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estadio deve conter no mínimo 3 letras.");
        }
    };

    private void validateHomeClub(Clube clubeAssociado) {
        try {
            clubeRepository.findById(clubeAssociado.getId());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estadio precisa estar vinculado a um clube existente e ativo.");
        }
    }
}
