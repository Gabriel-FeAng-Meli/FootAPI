package com.meli.footapi.dto;

import java.util.ArrayList;
import java.util.List;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RetrospectivaDto {

    private int golsFeitos;
    private int golsRecebidos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int partidasJogadas;

    public RetrospectivaDto(Clube clube, List<Partida> partidasJogadas) {        

        int vitorias = 0;
        int derrotas = 0;
        int empates = 0;
        int golsFeitos = 0;
        int golsRecebidos = 0; 

        final int idClube = clube.getId();

        for (int i = 0; i < partidasJogadas.size(); i++) {
            Partida p = partidasJogadas.get(i);
            final int idCasa = p.getClubeDaCasa().getId();
            int golsCasa = p.getGolsClubeDaCasa();
            final int idVisitante = p.getClubeVisitante().getId();
            int golsVisitante = p.getGolsClubeVisitante();

            if (idClube == idCasa) {
                golsFeitos += golsCasa;
                golsRecebidos += golsVisitante;
                if (golsFeitos > golsRecebidos) {
                    vitorias += 1;
                } else if (golsFeitos == golsRecebidos) {
                    empates += 1;
                } else {
                    derrotas += 1;
                }
            }

            if (idClube == idVisitante) {
                golsFeitos += golsVisitante;
                golsRecebidos += golsCasa;
                if (golsFeitos > golsRecebidos) {
                    vitorias += 1;
                } else if (golsFeitos == golsRecebidos) {
                    empates += 1;
                } else {
                    derrotas += 1;
                }
            }
        }

        this.derrotas = derrotas;
        this.empates = empates;
        this.vitorias = vitorias;
        this.golsFeitos = golsFeitos;
        this.golsRecebidos = golsRecebidos;
        this.partidasJogadas = vitorias + derrotas + empates;
    }

}
