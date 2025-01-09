package com.meli.footapi.dto;

import org.modelmapper.ModelMapper;

import com.meli.footapi.entity.Partida;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartidaDto {

    private int id;
    private ClubeDto clubeDaCasa;
    private int golsClubeDaCasa;
    private ClubeDto clubeVisitante;
    private int golsClubeVisitante;
    private EstadioDto estadio;

    public static Partida dtoToPartida(PartidaDto dto) {
        ModelMapper mapper = new ModelMapper();
        
        Partida partida = mapper.map(dto, Partida.class);

        return partida;
    }


    public static PartidaDto partidaToDto(Partida partida) {
        ModelMapper mapper = new ModelMapper();

        PartidaDto partidaDto = mapper.map(partida, PartidaDto.class);

        return partidaDto;
    }
}
