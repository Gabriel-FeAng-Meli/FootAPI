package com.meli.footapi.validation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.enums.ValidBrazilStates;
import com.meli.footapi.repository.ClubeRepository;

@Service
public class ClubeValidation {

    @Autowired
    ClubeRepository clubeRepository;

    public void validateClubInput(Clube clubToValidate) {

        
        String nomeDoClube = clubToValidate.getNome();
        String estadoDoClube = clubToValidate.getEstado();
        validateClubName(nomeDoClube);
        validateClubState(estadoDoClube);
        validateClubDate(clubToValidate.getDataDeCriacao());
        checkIfClubAlreadyExists(clubeRepository.findAll(), nomeDoClube, estadoDoClube);
    }


    private void validateClubDate(LocalDate inputedDate) {
        if (inputedDate == null || inputedDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de criação do clube não pode ser no futuro. Insira uma data valida no formato YYYY-MM-DD");
        }
    }

    private void validateClubState(String inputedState) {
        boolean validState = false;
        for (ValidBrazilStates state : ValidBrazilStates.values()) {
            if (inputedState != null && state.toString().equals(inputedState.toUpperCase())) {
                validState = true;
            }
        }
        if (!validState) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado escolhido não é um estado real do Brasil. Favor utilizar a sigla de um estado existente (exemplo: SP)");
        };
    }

    private void validateClubName(String inputedName) {
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do clube deve conter no mínimo 2 letras.");
        }
    }

    private void checkIfClubAlreadyExists(List<Clube> existingClubs, String inputedName, String inputedState) {
        existingClubs.stream().forEach(existingClub -> {
            if(existingClub.getNome().equals(inputedName) && existingClub.getEstado().equals(inputedState)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um clube de mesmo nome e mesmo estado já está cadastrado.");
            }
        });
    }
}
