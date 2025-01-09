package com.meli.footapi.dto;

import com.meli.footapi.entity.Stadium;
import lombok.Data;

@Data
public class StadiumDto {
    private int id;
    private String name;
    
    public static Stadium dtoToStadium(StadiumDto dto) {

        Stadium resultingStadium = new Stadium();
        resultingStadium.setId(dto.getId());
        resultingStadium.setName(dto.getName());

        return resultingStadium;
    }
    public static StadiumDto stadiumToDto(Stadium stadium) {

        StadiumDto resultingStadiumDto = new StadiumDto();

        resultingStadiumDto.setId(stadium.getId());
        resultingStadiumDto.setName(stadium.getName());
        return resultingStadiumDto;
    }
}
