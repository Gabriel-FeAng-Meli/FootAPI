package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.EstadioRepository;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository stadiumRepo;

    public EstadioDto createStadium(EstadioDto stadiumDto) {
        validateStadiumInput(stadiumDto);

        Estadio stadium = EstadioDto.dtoToEstadio(stadiumDto);
        stadiumRepo.save(stadium);

        return stadiumDto;
    }

    public List<EstadioDto> getStadiums() {
        List<EstadioDto> dtoList = new ArrayList<>();
        List<Estadio> stadiumList = stadiumRepo.findAll();
    
        for (int i = 0; i < stadiumList.size(); i++) {
            Estadio c = stadiumList.get(i);
            dtoList.add(EstadioDto.estadioToDto(c));
        }

        return dtoList;
    }

    public EstadioDto getStadiumById(int id) {

        Estadio stadium = this.stadiumRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum estádio com o ID " + id));

        EstadioDto dto = EstadioDto.estadioToDto(stadium);

        return dto;
    }

    public EstadioDto updateStadium(int id, EstadioDto updatedStadiumInfo) {
        EstadioDto stadiumToBeUpdated = getStadiumById(id);

        validateStadiumInput(updatedStadiumInfo);

        stadiumToBeUpdated.setId(id);
        stadiumToBeUpdated.setName(updatedStadiumInfo.getName());

        Estadio updatedStadium = EstadioDto.dtoToEstadio(stadiumToBeUpdated);
        stadiumRepo.save(updatedStadium);

        return stadiumToBeUpdated;
    }

    public void deleteStadium(int id) {
        Estadio stadiumToDelete = stadiumRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum estádio com o ID " + id));

        stadiumRepo.delete(stadiumToDelete);

    }


    private void validateStadiumInput(EstadioDto estadioParaValidar) {

        String inputedName = estadioParaValidar.getName();
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estadio deve conter no mínimo 3 letras.");
        }

        List<EstadioDto> estadiosExistentes = getStadiums();
        estadiosExistentes.stream().forEach(estadio -> {
            if(estadio.getName().equals(inputedName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um estadio de mesmo nome já está cadastrado.");
            }
        });
    };

}

