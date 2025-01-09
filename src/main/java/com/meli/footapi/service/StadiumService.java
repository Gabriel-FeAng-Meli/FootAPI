package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.StadiumRepo;


@Service
public class StadiumService {

    @Autowired
    private StadiumRepo stadiumRepo;

    public EstadioDto createStadium(EstadioDto stadiumDto) {
        validateStadiumInput(stadiumDto);

        Estadio stadium = EstadioDto.dtoToStadium(stadiumDto);
        stadiumRepo.save(stadium);

        return stadiumDto;
    }

    public List<EstadioDto> getStadiums() {
        List<EstadioDto> dtoList = new ArrayList<>();
        List<Estadio> stadiumList = stadiumRepo.findAll();
    
        for (int i = 0; i < stadiumList.size(); i++) {
            Estadio c = stadiumList.get(i);
            dtoList.add(EstadioDto.stadiumToDto(c));
        }

        return dtoList;
    }

    public EstadioDto getStadiumById(int id) {

        Estadio stadium = this.stadiumRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum estádio com o ID " + id));

        EstadioDto dto = EstadioDto.stadiumToDto(stadium);

        return dto;
    }

    public EstadioDto updateStadium(int id, EstadioDto updatedStadiumInfo) {
        EstadioDto stadiumToBeUpdated = getStadiumById(id);

        validateStadiumInput(updatedStadiumInfo);

        stadiumToBeUpdated.setId(id);
        stadiumToBeUpdated.setName(updatedStadiumInfo.getName());

        Estadio updatedStadium = EstadioDto.dtoToStadium(stadiumToBeUpdated);
        stadiumRepo.save(updatedStadium);

        return stadiumToBeUpdated;
    }

    public void deleteStadium(int id) {
        Estadio stadiumToDelete = stadiumRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum estádio com o ID " + id));

        stadiumRepo.delete(stadiumToDelete);

    }


    private void validateStadiumInput(EstadioDto stadiumToValidate) {
        // fazer validações que dão throw se der errado
    };

}

