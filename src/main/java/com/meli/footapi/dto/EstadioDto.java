package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import lombok.Data;

@Data
public class EstadioDto {
    private int id;
    private String name;
    private Clube clube;
    
    public static Estadio dtoToStadium(EstadioDto dto) {

        Estadio resultingStadium = new Estadio();
        resultingStadium.setId(dto.getId());
        resultingStadium.setName(dto.getName());
        resultingStadium.setClube(dto.getClube());

        return resultingStadium;
    }
    public static EstadioDto stadiumToDto(Estadio stadium) {

        EstadioDto resultingStadiumDto = new EstadioDto();

        resultingStadiumDto.setId(stadium.getId());
        resultingStadiumDto.setName(stadium.getName());
        resultingStadiumDto.setClube(stadium.getClube());
        return resultingStadiumDto;
    }
}
