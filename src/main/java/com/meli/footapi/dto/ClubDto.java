package com.meli.footapi.dto;

import java.time.LocalDate;

import com.meli.footapi.entity.Club;

import lombok.Data;

@Data
public class ClubDto {
    public static Club dtoToClub(ClubDto dto) {

        Club resultingClub = new Club();
        resultingClub.setActive(dto.isActive());
        resultingClub.setDate(dto.getDate());
        resultingClub.setId(dto.getId());
        resultingClub.setName(dto.getName());
        resultingClub.setState(dto.getState());

        return resultingClub;
    }
    public static ClubDto clubToDto(Club club) {

        ClubDto resultingClubDto = new ClubDto();
        resultingClubDto.setActive(club.isActive());
        resultingClubDto.setDate(club.getDate());
        resultingClubDto.setId(club.getId());
        resultingClubDto.setState(club.getState());
        resultingClubDto.setName(club.getName());

        return resultingClubDto;
    }
    private int id;
    private String name;
    private String state;
    private boolean active;
    private LocalDate date;

}
