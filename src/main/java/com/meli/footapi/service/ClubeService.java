package com.meli.footapi.service;

import com.meli.footapi.enums.ValidBrazilStates;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.repository.ClubeRepository;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class ClubeService {

    @Autowired
    protected ClubeRepository clubRepo;

    public ClubeDto createClub(Clube clube) {
        validateClubInput(clube);
        clubRepo.save(clube);

        int id = clube.getId();

        return getClubById(id);
    }

    public List<ClubeDto> getClubs() {

        ModelMapper mapper = new ModelMapper();
        List<Clube> clubList = clubRepo.findAll();

        List<ClubeDto> dtoList = new ArrayList<>();

        clubList.forEach(clube -> {
            ClubeDto dto = mapper.map(clube, ClubeDto.class);
            dtoList.add(dto);
        });
    
        return dtoList;
    }

    public ClubeDto getClubById(int id) {

        ModelMapper mapper = new ModelMapper();

        Clube club = this.clubRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum clube com o ID " + id));

        ClubeDto dto = mapper.map(club, ClubeDto.class);

        return dto;
    }

    public ClubeDto updateClub(int id, Clube updatedClubInfo) {
        ClubeDto clubToBeUpdated = getClubById(id);

        ModelMapper mapper = new ModelMapper();
        
        validateClubInput(updatedClubInfo);

        clubToBeUpdated.setNome(updatedClubInfo.getNome());
        clubToBeUpdated.setEstado(updatedClubInfo.getEstado());
        clubToBeUpdated.setAtivo(updatedClubInfo.isAtivo());
        clubToBeUpdated.setDataDeCriacao(updatedClubInfo.getDataDeCriacao());

        Clube updatedClub = mapper.map(clubToBeUpdated, Clube.class);
        clubRepo.save(updatedClub);

        return clubToBeUpdated;
    }

    public void deleteClub(int id) {
        Clube clubToDelete = clubRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum clube com o id " + id));

        clubRepo.delete(clubToDelete);

    }

    public RetrospectivaDto getRetrospectiva(int clubId, List<Partida> partidas) {
        Clube clube = clubRepo.findById(clubId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        RetrospectivaDto retro = new RetrospectivaDto(clube, partidas);

        return retro;
    }



    private void validateClubInput(Clube clubToValidate) {
        String inputedName = clubToValidate.getNome();
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do clube deve conter no mínimo 3 letras.");
        }

        String inputedState = clubToValidate.getEstado();
        boolean validState = false;
        for (ValidBrazilStates state : ValidBrazilStates.values()) {
            if (inputedState != null && state.toString().equals(inputedState.toUpperCase())) {
                validState = true;
            }
        }
        if (!validState) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado escolhido não é um estado real do Brasil. Favor utilizar a sigla de um estado existente (exemplo: SP)");
        }

        LocalDate inputedDate = clubToValidate.getDataDeCriacao();
        if (inputedDate != null && inputedDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de criação do clube não pode ser no futuro. Insira uma data valida no formato YYYY-MM-DD");
        }

        List<ClubeDto> existingClubs = getClubs();
        existingClubs.stream().forEach(existingClub -> {
            if(existingClub.getNome().equals(inputedName) && existingClub.getEstado().equals(inputedState)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um clube de mesmo nome e mesmo estado já está cadastrado.");
            }
        });


    }

}
