package com.meli.footapi.dto;

import com.meli.footapi.entity.Match;

import lombok.*;

@Data
public class MatchDto {

    public static Match dtoToMatch(MatchDto dto) {

        Match resultingMatch = new Match();
        resultingMatch.setId(dto.getId());


        return resultingMatch;
    }
    public static MatchDto matchToDto(Match match) {

        MatchDto resultingMatchDto = new MatchDto();
        resultingMatchDto.setId(match.getId());

        return resultingMatchDto;
    }
    private int id;

}
