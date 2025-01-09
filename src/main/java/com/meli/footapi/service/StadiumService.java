package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.StadiumDto;
import com.meli.footapi.entity.Stadium;
import com.meli.footapi.repository.StadiumRepo;


@Service
public class StadiumService {

    @Autowired
    private StadiumRepo stadiumRepo;

    public StadiumDto createStadium(StadiumDto stadiumDto) {
        validateStadiumInput(stadiumDto);

        Stadium stadium = StadiumDto.dtoToStadium(stadiumDto);
        stadiumRepo.save(stadium);

        return stadiumDto;
    }

    public List<StadiumDto> getStadiums() {
        List<StadiumDto> dtoList = new ArrayList<>();
        List<Stadium> stadiumList = stadiumRepo.findAll();
    
        for (int i = 0; i < stadiumList.size(); i++) {
            Stadium c = stadiumList.get(i);
            dtoList.add(StadiumDto.stadiumToDto(c));
        }

        return dtoList;
    }

    public StadiumDto getStadiumById(int id) {

        Stadium stadium = this.stadiumRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum estádio com o ID " + id));

        StadiumDto dto = StadiumDto.stadiumToDto(stadium);

        return dto;
    }

    public StadiumDto updateStadium(int id, StadiumDto updatedStadiumInfo) {
        StadiumDto stadiumToBeUpdated = getStadiumById(id);

        validateStadiumInput(updatedStadiumInfo);

        stadiumToBeUpdated.setId(id);
        stadiumToBeUpdated.setName(updatedStadiumInfo.getName());

        Stadium updatedStadium = StadiumDto.dtoToStadium(stadiumToBeUpdated);
        stadiumRepo.save(updatedStadium);

        return stadiumToBeUpdated;
    }

    public void deleteStadium(int id) {
        Stadium stadiumToDelete = stadiumRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum estádio com o ID " + id));

        stadiumRepo.delete(stadiumToDelete);

    }


    private void validateStadiumInput(StadiumDto stadiumToValidate) {
        // fazer validações que dão throw se der errado
    };

}

