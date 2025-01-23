package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class BuscaAvancadaService {
    @Autowired
    private ClubeService clubeService;

    @Autowired
    private PartidaService partidaService;

    public RetrospectivaDto getRetrospectiva(int clubId) {
        Clube clube = ClubeDto.dtoToClube(clubeService.getClubeById(clubId));
        String titulo = clube.getNome() + "-" + clube.getEstado();

        List<Partida> listaDePartidas = partidaService.getPartidasByClube(clube);
        
        RetrospectivaDto retro = new RetrospectivaDto(clube, listaDePartidas);
        retro.setTitulo(titulo);

        return retro;
    }

    public RetrospectivaDto getConfrontosDiretos(int clubId, int otherClubId) {
        Clube clubeUm = ClubeDto.dtoToClube(clubeService.getClubeById(clubId));
        Clube clubeDois = ClubeDto.dtoToClube(clubeService.getClubeById(otherClubId));
        
        String titulo = clubeUm.getNome() + "-" + clubeUm.getEstado() + " X " + clubeDois.getNome() + "-" + clubeDois.getEstado();
        
        RetrospectivaDto retro = getRetrospectiva(clubId);
        retro.setTitulo(titulo);

        return retro;
    }
    
    public List<RetrospectivaDto> getRetrospectivaParaCadaAdversario(int clubId) {
        Clube clube = ClubeDto.dtoToClube(clubeService.getClubeById(clubId));
        
        Set<Integer> idTimesEnfrentados = RetrospectivaDto.getTimesEnfrentadosPorClube(clube, partidaService.getPartidasByClube(clube));
        
        List<RetrospectivaDto> retrospectivas = new ArrayList<>();
        
        idTimesEnfrentados.stream().forEach(id -> {
            retrospectivas.add(getConfrontosDiretos(clubId, id));
        });

        return retrospectivas;
    }

    public List<RankingDto> getRanking() {
        List<RankingDto> unsortedRanking= new ArrayList<>();
        
        List<ClubeDto> todosOsClubes = clubeService.listarClubesAtivos();

        todosOsClubes.forEach(clube -> {
            int id = clube.getId();
            RetrospectivaDto retro = getRetrospectiva(id);
            RankingDto rank = new RankingDto();
            rank.setClube(clube);
            rank.setPontuacao(retro.getPontuação());

            unsortedRanking.add(rank);
        });

        List<RankingDto> ranking = unsortedRanking.stream().sorted((r1, r2) -> Integer.compare(r2.getPontuacao(), r1.getPontuacao())).filter(rank -> rank.getPontuacao() != 0).toList();

        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setRank(i + 1);
        }

        return ranking;
    }

}
