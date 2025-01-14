package com.meli.footapi.dto;

import java.time.LocalDate;
import org.modelmapper.ModelMapper;

import com.meli.footapi.entity.Clube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClubeDto {

    private int id;
    private String nome;
    private String estado;
    private boolean ativo;
    private LocalDate dataDeCriacao;
    private static ModelMapper mapper = new ModelMapper();

    public static Clube dtoToClube(ClubeDto dto) {        
        Clube clube = mapper.map(dto, Clube.class);

        return clube;
    }


    public static ClubeDto clubeToDto(Clube clube) {
        ClubeDto clubeDto = mapper.map(clube, ClubeDto.class);

        return clubeDto;
    }

}
