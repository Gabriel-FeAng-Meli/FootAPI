package com.meli.footapi.dto;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClubeDto {

    private int id;
    private String name;
    private String state;
    private boolean active;
    private LocalDate date;
    private Estadio estadio;
    private List<Partida> partidasDeCasa;
    private List<Partida> partidaVisitante;


    public static Clube dtoToClube(ClubeDto dto) {
        ModelMapper mapper = new ModelMapper();
        
        Clube clube = mapper.map(dto, Clube.class);

        return clube;
    }


    public static ClubeDto clubeToDto(Clube clube) {
        ModelMapper mapper = new ModelMapper();

        ClubeDto clubeDto = mapper.map(clube, ClubeDto.class);

        return clubeDto;
    }

}
