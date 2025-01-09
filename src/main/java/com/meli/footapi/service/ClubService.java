package com.meli.footapi.service;

import com.meli.footapi.enums.ValidBrazilStates;
import com.meli.footapi.entity.Club;
import com.meli.footapi.dto.ClubDto;
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

    public ClubDto createClub(ClubDto clubDto) {
        validateClubInput(clubDto);

        Club club = ClubDto.dtoToClub(clubDto);
        clubRepo.save(club);

        return clubDto;
    }

    public List<ClubDto> getClubs() {
        List<ClubDto> dtoList = new ArrayList<>();
        List<Club> clubList = clubRepo.findAll();
    
        for (int i = 0; i < clubList.size(); i++) {
            Club c = clubList.get(i);
            dtoList.add(ClubDto.clubToDto(c));
        }

        return dtoList;
    }

    public ClubDto getClubById(int id) {

        Club club = this.clubRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum clube com o ID " + id));

        ClubDto dto = ClubDto.clubToDto(club);

        return dto;
    }

    public ClubDto updateClub(int id, ClubDto updatedClubInfo) {
        ClubDto clubToBeUpdated = getClubById(id);

        validateClubInput(updatedClubInfo);

        clubToBeUpdated.setName(updatedClubInfo.getName());
        clubToBeUpdated.setState(updatedClubInfo.getState());
        clubToBeUpdated.setActive(updatedClubInfo.isActive());
        clubToBeUpdated.setDate(updatedClubInfo.getDate());


        Club updatedClub = ClubDto.dtoToClub(clubToBeUpdated);
        clubRepo.save(updatedClub);

        return clubToBeUpdated;
    }

    public void deleteClub(int id) {
        Club clubToDelete = clubRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum clube com o id " + id));

        clubRepo.delete(clubToDelete);

    }


    private void validateClubInput(ClubDto clubToValidate) {
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

        List<ClubDto> existingClubs = getClubs();
        existingClubs.stream().forEach(existingClub -> {
            if(existingClub.getName().equals(inputedName) && existingClub.getState().equals(inputedState)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um clube de mesmo nome e mesmo estado já está cadastrado.");
            }
        });


    }

}
