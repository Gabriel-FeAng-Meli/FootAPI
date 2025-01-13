package com.meli.footapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meli.footapi.entity.Partida;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartidaDto {

    private int id;
    private String clubeDaCasaNome;
    private int golsClubeDaCasa;
    private String clubeVisitanteNome;
    private int golsClubeVisitante;
    private String estadioNome;

    @JsonIgnore
    private boolean goleada;
    
    public static PartidaDto partidaToDto(Partida partida) {
        PartidaDto partidaDto = new PartidaDto();

        partidaDto.setId(partida.getId());
        partidaDto.setGolsClubeDaCasa(partida.getGolsClubeDaCasa());
        partidaDto.setGolsClubeVisitante(partida.getGolsClubeVisitante());
        partidaDto.setEstadioNome(partida.getEstadio().getNome());
        partidaDto.setClubeDaCasaNome(partida.getClubeDaCasa().getNome() + " - " + partida.getClubeDaCasa().getEstado());
        partidaDto.setClubeVisitanteNome(partida.getClubeVisitante().getNome() + " - " + partida.getClubeVisitante().getEstado());

        int golsCasa = partida.getGolsClubeDaCasa();
        int golsVis = partida.getGolsClubeVisitante();

        int diferençaGols = golsCasa - golsVis;
        
        if (diferençaGols >= 3 || diferençaGols <= -3) {
            partidaDto.setGoleada(true);
        } else {
            partidaDto.setGoleada(false);
        }

        return partidaDto;
    }
}
