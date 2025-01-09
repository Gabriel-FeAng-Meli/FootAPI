package com.meli.footapi.dto;

import com.meli.footapi.entity.Partida;

import lombok.*;

@Data
public class PartidaDto {

    public static Partida dtoToMatch(PartidaDto dto) {

        Partida resultingMatch = new Partida();
        resultingMatch.setId(dto.getId());


        return resultingMatch;
    }
    public static PartidaDto matchToDto(Partida match) {

        PartidaDto resultingMatchDto = new PartidaDto();
        resultingMatchDto.setId(match.getId());

        return resultingMatchDto;
    }
    private int id;

}
