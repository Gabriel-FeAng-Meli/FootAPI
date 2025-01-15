package com.meli.footapi.dto;

import java.time.LocalDate;

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

    public static Clube dtoToClube(ClubeDto dto) {        
        Clube clube = new Clube(dto.getId(), dto.getNome(), dto.getEstado(), dto.isAtivo(), dto.getDataDeCriacao());

        return clube;
    }

    public static ClubeDto clubeToDto(Clube clube) {
        ClubeDto dto = new ClubeDto(clube.getId(), clube.getNome(), clube.getEstado(), clube.isAtivo(), clube.getDataDeCriacao());

        return dto;
    }

}
