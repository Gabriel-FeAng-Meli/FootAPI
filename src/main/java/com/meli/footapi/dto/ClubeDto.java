package com.meli.footapi.dto;

import java.time.LocalDate;

import com.meli.footapi.entity.Clube;

import lombok.Data;

@Data
public class ClubeDto {
    public static Clube dtoToClub(ClubeDto dto) {

        Clube resultingClub = new Clube();
        resultingClub.setActive(dto.isActive());
        resultingClub.setDate(dto.getDate());
        resultingClub.setId(dto.getId());
        resultingClub.setNome(dto.getName());
        resultingClub.setState(dto.getState());

        return resultingClub;
    }
    public static ClubeDto clubToDto(Clube club) {

        ClubeDto resultingClubDto = new ClubeDto();
        resultingClubDto.setActive(club.isActive());
        resultingClubDto.setDate(club.getDate());
        resultingClubDto.setId(club.getId());
        resultingClubDto.setState(club.getState());
        resultingClubDto.setName(club.getNome());

        return resultingClubDto;
    }
    private int id;
    private String name;
    private String state;
    private boolean active;
    private LocalDate date;

}
