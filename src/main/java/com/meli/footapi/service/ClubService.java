package com.meli.footapi.service;

import com.meli.footapi.enums.ValidBrazilStates;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.repository.ClubRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class ClubService {

    @Autowired
    private ClubRepo clubRepo;

    public ClubeDto createClub(ClubeDto clubDto) {
        validateClubInput(clubDto);

        Clube club = ClubeDto.dtoToClub(clubDto);
        
        clubRepo.save(club);


        return clubDto;
    }

    public List<ClubeDto> getClubs() {
        List<ClubeDto> dtoList = new ArrayList<>();
        List<Clube> clubList = clubRepo.findAll();
    
        for (int i = 0; i < clubList.size(); i++) {
            Clube c = clubList.get(i);
            dtoList.add(ClubeDto.clubToDto(c));
        }

        return dtoList;
    }

    public ClubeDto getClubById(int id) {

        Clube club = this.clubRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum clube com o ID " + id));

        ClubeDto dto = ClubeDto.clubToDto(club);

        return dto;
    }

    public ClubeDto updateClub(int id, ClubeDto updatedClubInfo) {
        ClubeDto clubToBeUpdated = getClubById(id);

        validateClubInput(updatedClubInfo);

        clubToBeUpdated.setName(updatedClubInfo.getName());
        clubToBeUpdated.setState(updatedClubInfo.getState());
        clubToBeUpdated.setActive(updatedClubInfo.isActive());
        clubToBeUpdated.setDate(updatedClubInfo.getDate());


        Clube updatedClub = ClubeDto.dtoToClub(clubToBeUpdated);
        clubRepo.save(updatedClub);

        return clubToBeUpdated;
    }

    public void deleteClub(int id) {
        Clube clubToDelete = clubRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum clube com o id " + id));

        clubRepo.delete(clubToDelete);

    }


    private void validateClubInput(ClubeDto clubToValidate) {
        String inputedName = clubToValidate.getName();
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do clube deve conter no mínimo 3 letras.");
        }

        String inputedState = clubToValidate.getState();
        boolean validState = false;
        for (ValidBrazilStates state : ValidBrazilStates.values()) {
            if (inputedState != null && state.toString().equals(inputedState.toUpperCase())) {
                validState = true;
            }
        }
        if (!validState) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado escolhido não é um estado real do Brasil. Favor utilizar a sigla de um estado existente (exemplo: SP)");
        }

        LocalDate inputedDate = clubToValidate.getDate();
        if (inputedDate != null && inputedDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de criação do clube não pode ser no futuro. Insira uma data valida no formato YYYY-MM-DD");
        }

        List<ClubeDto> existingClubs = getClubs();
        existingClubs.stream().forEach(existingClub -> {
            if(existingClub.getName().equals(inputedName) && existingClub.getState().equals(inputedState)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um clube de mesmo nome e mesmo estado já está cadastrado.");
            }
        });


    }

}
