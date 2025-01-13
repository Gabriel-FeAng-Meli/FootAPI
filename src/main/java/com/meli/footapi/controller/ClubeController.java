package com.meli.footapi.controller;


import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.ClubeService;
import com.meli.footapi.service.PartidaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
      



@RestController
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubService;

    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<ClubeDto> createClub(@RequestBody Clube club) {
        return ResponseEntity.status(201).body(clubService.createClub(club));
    }

    @GetMapping("/{id}")
    public ClubeDto getClubById(@PathVariable(value = "id") int clubId) {
        return clubService.getClubById(clubId);
    }

    public List<ClubeDto> getClubes() {
        return clubService.getClubs();
    }

    @GetMapping("/{id}/retrospectiva")
    public RetrospectivaDto getRetrospectiva(@PathVariable(value = "id") int clubId) {
        Clube clube = ClubeDto.dtoToClube(clubService.getClubById(clubId));
        List<Partida> listaDePartidas = partidaService.getPartidasByClube(clube);
        String titulo = clube.getNome() + "-" + clube.getEstado();
        return clubService.getRetrospectiva(clubId, listaDePartidas, titulo);
    }

    @GetMapping("/{id1}/retrospectiva/{id2}")
    public RetrospectivaDto getRetrospectivaIndividual(@PathVariable(value = "id1") int clubId, @PathVariable(value = "id2") int otherClubId) {
        Clube clubeUm = ClubeDto.dtoToClube(clubService.getClubById(clubId));
        Clube clubeDois = ClubeDto.dtoToClube(clubService.getClubById(otherClubId));

        List<Partida> partidasEntreOsClubes = partidaService.getPartidasEntreDoisClubes(clubeUm, clubeDois);
        String titulo = clubeUm.getNome() + "-" + clubeUm.getEstado() + " X " + clubeDois.getNome() + "-" + clubeDois.getEstado();
        return clubService.getRetrospectiva(clubId, partidasEntreOsClubes, titulo);
    }

    @GetMapping("/{id}/retrospectiva/clubes")
    public List<RetrospectivaDto> getRetrospectivaParaCadaAdversario(@PathVariable(value = "id") int clubId) {
        
        Clube clube = ClubeDto.dtoToClube(clubService.getClubById(clubId));
        
        Set<Integer> idTimesEnfrentados = RetrospectivaDto.getTimesEnfrentadosPorClube(clube, partidaService.getPartidasByClube(clube));
        
        List<RetrospectivaDto> retrospectivas = new ArrayList<>();
        
        idTimesEnfrentados.stream().forEach(id -> {
            retrospectivas.add(getRetrospectivaIndividual(clubId, id));
        });

        return retrospectivas;
    }
    
    @GetMapping("/ranking")
    public List<RankingDto> getRanking() {
        List<RankingDto> unsortedRanking= new ArrayList<>();
        
        List<ClubeDto> todosOsClubes = clubService.getClubs();

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


    @GetMapping
    public ResponseEntity<Map<String, Object>> getPaginatedClubes(
        @RequestParam(required = false) String nome,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        try {
            List<Clube> clubes = new ArrayList<Clube>();
            Page<Clube> paginaClube;
            if(nome == null)
                paginaClube = clubService.findAll(size, page);
            else {
                paginaClube = clubService.findByNomeDoClube(nome, size, page);
            }

            clubes = paginaClube.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Clubes", clubes);
            response.put("paginaAtual", paginaClube.getNumber() + 1);
            response.put("totalDeItens", paginaClube.getTotalElements());
            response.put("totalDePaginas", paginaClube.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel realizar a busca com os parametros fornecidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(@PathVariable(value = "id") int clubId, @RequestBody Clube newClubInfo) {
        return ResponseEntity.status(204).body(clubService.updateClub(clubId, newClubInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable(value = "id") int clubId) {
        clubService.deleteClub(clubId);

        return ResponseEntity.noContent().build();
    }
}
