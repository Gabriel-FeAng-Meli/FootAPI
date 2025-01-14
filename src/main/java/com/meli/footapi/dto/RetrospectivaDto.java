package com.meli.footapi.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RetrospectivaDto {

    private String titulo;
    private int golsFeitos;
    private int golsRecebidos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int totalDePartidas;
    private int pontuação;

    public RetrospectivaDto(Clube clube, List<Partida> partidasJogadas) {        
        this.titulo = "Retrospectiva " + clube.getNome() + " - " + clube.getEstado();

        partidasJogadas.stream().forEach(partida -> {
            getResultadoDaPartida(clube.getId(), partida);
        });

    }

    public void getResultadoDaPartida(int idClube, Partida partida) {
        final int idClubeDaCasaNaPartida = partida.getClubeDaCasa().getId();

        int golsCasa = partida.getGolsClubeDaCasa();
        int golsVisitante = partida.getGolsClubeVisitante();

        if (idClube == idClubeDaCasaNaPartida) {
            this.golsFeitos += golsCasa;
            this.golsRecebidos += golsVisitante;
            addResultados(golsCasa, golsVisitante);       
        } else {
            this.golsFeitos += golsVisitante;
            this.golsRecebidos += golsCasa;
            addResultados(golsCasa, golsVisitante);
        }
    }

    public void addResultados(int golsDoClube, int golsDoAdversario) {
        this.totalDePartidas += 1;
        if (golsDoClube > golsDoAdversario) {
            this.vitorias += 1;
            this.pontuação += 3;
        } else if (golsDoClube == golsDoAdversario) {
            this.empates += 1;
            this.pontuação += 1;
        } else {
            this.derrotas += 1;
        }
    } 

    public static Set<Integer> getTimesEnfrentadosPorClube(Clube clube, List<Partida> partidasJogadas) {
        Set<Integer> listaDeTimesEnfrentados = new HashSet<>();
        
        final int idClube = clube.getId();

        partidasJogadas.stream().forEach(p -> {
            int idCasa = p.getClubeDaCasa().getId();
            if(idCasa == idClube) {
                listaDeTimesEnfrentados.add(p.getClubeVisitante().getId());
            } else {
                listaDeTimesEnfrentados.add(idCasa);
            }
        });

        return listaDeTimesEnfrentados;
    }
}
