package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.PartidaRepository;
import com.meli.footapi.validation.PartidaValidation;


@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private PartidaValidation partidaValidation;

    public PartidaDto createPartida(Partida partida) {
        partidaValidation.validateMatchInput(partida);

        if (partida.getGolsClubeDaCasa() - partida.getGolsClubeVisitante() >= 3 || partida.getGolsClubeVisitante() - partida.getGolsClubeDaCasa() >= 3) {
            partida.setGoleada(true);
        }

        partidaRepository.save(partida);

        int id = partida.getId();

        return getMatchById(id);
    }

    public Map<String, Object> paginarPartidas(Boolean goleada, String nomeClubeDaCasa, String nomeClubeVisitante, int pagina, int limite) {
        try {
            Map<String, Object> response = new HashMap<>();

            Page<Partida> paginaPartida;
            if(goleada != null)
                paginaPartida = getPartidasPaginadasByGoleada(goleada, pagina, limite);
            else if(nomeClubeDaCasa != null)
                paginaPartida = getPartidasPaginadasByNomeDoClubeDaCasa(nomeClubeDaCasa, pagina, limite);
            else if(nomeClubeVisitante != null)
                paginaPartida = getPartidasPaginadasByNomeDoClubeVisitante(nomeClubeVisitante, pagina, limite);
            else {
                paginaPartida = getPartidasPaginadas(pagina, limite);                
            }

            List<Partida> partidas = paginaPartida.getContent();
            
            response.put("Partidas", partidas);
            response.put("paginaAtual", paginaPartida.getNumber() + 1);
            response.put("totalDeItens", paginaPartida.getTotalElements());
            response.put("totalDePaginas", paginaPartida.getTotalPages());

            return response;
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    public Page<Partida> getPartidasPaginadasByGoleada(boolean goleada, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = partidaRepository.findByGoleada(goleada, paginacao);

        return partidas;
    }

    public Page<Partida> getPartidasPaginadasByNomeDoClubeDaCasa(String nome, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = partidaRepository.findByClubeDaCasaNomeContains(nome, paginacao);

        return partidas;
    }


    public Page<Partida> getPartidasPaginadasByNomeDoClubeVisitante(String nome, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = partidaRepository.findByClubeVisitanteNomeContains(nome, paginacao);

        return partidas;
    }

    public Page<Partida> getPartidasPaginadas(int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = partidaRepository.findAll(paginacao);

        return partidas;
    }

    public PartidaDto getMatchById(int id) {

        Partida match = this.partidaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrada nenhuma partida com o ID " + id));

        PartidaDto dto = PartidaDto.partidaToDto(match);

        return dto;
    }

    public PartidaDto updateMatch(int id, Partida dadosAtualizados) {
        Partida partida = this.partidaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi encontrada nenhuma partida com ID" + id + ". Portanto, deve ser primeiro criada uma partida com este identificador, para então modifica-la."));
        partidaValidation.validateMatchInput(partida);

        partida.setClubeDaCasa(dadosAtualizados.getClubeDaCasa());
        partida.setClubeVisitante(dadosAtualizados.getClubeVisitante());
        partida.setEstadio(dadosAtualizados.getEstadio());
        partida.setGolsClubeDaCasa(dadosAtualizados.getGolsClubeDaCasa());
        partida.setGolsClubeVisitante(dadosAtualizados.getGolsClubeVisitante());
        partida.setEstadio(dadosAtualizados.getEstadio());

        if (partida.getGolsClubeDaCasa() - partida.getGolsClubeVisitante() >= 3 || partida.getGolsClubeVisitante() - partida.getGolsClubeDaCasa() >= 3) {
            partida.setGoleada(true);
        }

        partidaRepository.save(partida);

        return PartidaDto.partidaToDto(partida);
    }

    public void deleteMatch(int id) {
        Partida matchToDelete = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrada nenhuma partida com o id " + id));

        partidaRepository.delete(matchToDelete);

    }

    public List<Partida> getPartidasByClube(Clube clube) {
        List<Partida> lista = new ArrayList<>();

        lista.addAll(partidaRepository.findByClubeDaCasa(clube));
        lista.addAll(partidaRepository.findByClubeVisitante(clube));
        return lista;
    }

    public List<Partida> getPartidasEntreDoisClubes(Clube clubeUm, Clube clubeDois) {
        
        List<Partida> partidasClubeUm = partidaRepository.findByClubeDaCasa(clubeUm);
        partidasClubeUm.addAll(partidaRepository.findByClubeVisitante(clubeUm));
        
        List<Partida> partidasClubeDois = partidaRepository.findByClubeDaCasa(clubeDois);
        partidasClubeDois.addAll(partidaRepository.findByClubeVisitante(clubeDois));
        
        List<Partida> partidasEntreOsClubes = partidasClubeUm.stream().filter(partidasClubeDois::contains).toList();

        return partidasEntreOsClubes;
    }

}

