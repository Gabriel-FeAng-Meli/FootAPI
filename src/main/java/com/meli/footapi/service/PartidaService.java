package com.meli.footapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.PartidaRepository;


@Service
public class PartidaService {

    @Autowired
    private PartidaRepository matchRepo;

    @Autowired
    private ClubeService clubService;

    @Autowired
    private EstadioService estadioService;

    public PartidaDto createMatch(Partida partida) {
        validateMatchInput(partida);

        if (partida.getGolsClubeDaCasa() - partida.getGolsClubeVisitante() >= 3 || partida.getGolsClubeVisitante() - partida.getGolsClubeDaCasa() >= 3) {
            partida.setGoleada(true);
        }

        matchRepo.save(partida);

        int id = partida.getId();

        return getMatchById(id);
    }


    public Page<Partida> getPagedPartidaByGoleada(boolean goleada, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = matchRepo.findByGoleada(goleada, paginacao);

        return partidas;
    }

    public Page<Partida> getPagedByNomeClubeDaCasa(String nome, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = matchRepo.findByClubeDaCasaNomeContains(nome, paginacao);

        return partidas;
    }


    public Page<Partida> getPagedByNomeClubeVisitante(String nome, int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = matchRepo.findByClubeVisitanteNomeContains(nome, paginacao);

        return partidas;
    }

    public Page<Partida> getPagedPartidas(int page, int size) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Partida> partidas = matchRepo.findAll(paginacao);

        return partidas;
    }

    public PartidaDto getMatchById(int id) {

        Partida match = this.matchRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrada nenhuma partida com o ID " + id));

        PartidaDto dto = PartidaDto.partidaToDto(match);

        return dto;
    }

    public PartidaDto updateMatch(int id, Partida dadosAtualizados) {
        Partida partida = this.matchRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi encontrada nenhuma partida com ID" + id + ". Portanto, deve ser primeiro criada uma partida com este identificador, para então modifica-la."));
        validateMatchInput(partida);

        partida.setClubeDaCasa(dadosAtualizados.getClubeDaCasa());
        partida.setClubeVisitante(dadosAtualizados.getClubeVisitante());
        partida.setEstadio(dadosAtualizados.getEstadio());
        partida.setGolsClubeDaCasa(dadosAtualizados.getGolsClubeDaCasa());
        partida.setGolsClubeVisitante(dadosAtualizados.getGolsClubeVisitante());
        partida.setEstadio(dadosAtualizados.getEstadio());

        if (partida.getGolsClubeDaCasa() - partida.getGolsClubeVisitante() >= 3 || partida.getGolsClubeVisitante() - partida.getGolsClubeDaCasa() >= 3) {
            partida.setGoleada(true);
        }

        matchRepo.save(partida);

        return PartidaDto.partidaToDto(partida);
    }

    public void deleteMatch(int id) {
        Partida matchToDelete = matchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrada nenhuma partida com o id " + id));

        matchRepo.delete(matchToDelete);

    }

    public List<Partida> getPartidasByClube(Clube clube) {
        List<Partida> lista = new ArrayList<>();

        lista.addAll(matchRepo.findByClubeDaCasa(clube));
        lista.addAll(matchRepo.findByClubeVisitante(clube));
        return lista;
    }

    public List<Partida> getPartidasEntreDoisClubes(Clube clubeUm, Clube clubeDois) {
        
        List<Partida> partidasClubeUm = matchRepo.findByClubeDaCasa(clubeUm);
        partidasClubeUm.addAll(matchRepo.findByClubeVisitante(clubeUm));
        
        List<Partida> partidasClubeDois = matchRepo.findByClubeDaCasa(clubeDois);
        partidasClubeDois.addAll(matchRepo.findByClubeVisitante(clubeDois));
        
        List<Partida> partidasEntreOsClubes = partidasClubeUm.stream().filter(partidasClubeDois::contains).toList();

        return partidasEntreOsClubes;
    }

    private void validateMatchInput(Partida partidaParaValidar) throws ResponseStatusException {
        LocalDateTime inputedDate = partidaParaValidar.getDataPartida();
        
        Clube clubeDaCasaInput = partidaParaValidar.getClubeDaCasa();

        Clube clubeDaCasa = ClubeDto.dtoToClube(clubService.getClubById(clubeDaCasaInput.getId()));

        int idClubeDaCasa = clubeDaCasa.getId();

        Estadio estadioComIdDoInput = EstadioDto.dtoToEstadio(estadioService.getStadiumById(partidaParaValidar.getEstadio().getId()));

        int idClubeAssociadoAoEstadio = estadioComIdDoInput.getClube().getId();

        if (idClubeAssociadoAoEstadio != idClubeDaCasa) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O id do estadio inserido não é do estadio pertencente ao clube " + clubeDaCasa.getNome());
        }

        LocalDateTime dataCriaçãoClubeDaCasa = clubeDaCasa.getDataDeCriacao().atStartOfDay();
        List<Partida> partidasClubeDaCasa = matchRepo.findByClubeDaCasa(clubeDaCasa);


        partidasClubeDaCasa.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(48);
            LocalDateTime inferiorMargin = usedDate.minusHours(48);

            if(inputedDate.isAfter(inferiorMargin) && inputedDate.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cada clube só pode jogar uma partida a cada 48h. O Clube da Casa já tem uma partida cadastrada em " + usedDate);
            }
        });
        
        Clube clubeVisitanteInput = partidaParaValidar.getClubeVisitante();

        Clube clubeVisitante = ClubeDto.dtoToClube(clubService.getClubById(clubeVisitanteInput.getId()));
        LocalDateTime dataCriaçãoClubeVisitante = clubeVisitante.getDataDeCriacao().atStartOfDay();
        List<Partida> partidasClubeVisitante = matchRepo.findByClubeVisitante(clubeVisitante);

        partidasClubeVisitante.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(48);
            LocalDateTime inferiorMargin = usedDate.minusHours(48);

            if(inputedDate.isAfter(inferiorMargin) && inputedDate.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cada clube só pode jogar uma partida a cada 48h. O Clube Visitante já tem uma partida cadastrada em " + usedDate);
            }
        });

        if(dataCriaçãoClubeDaCasa.isAfter(inputedDate) || dataCriaçãoClubeVisitante.isAfter(inputedDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A partida só pode ocorrer em uma data posterior à criação de ambos os clubes!");
        }

        List<Partida> partidasNoEstadio = matchRepo.findByEstadio(partidaParaValidar.getEstadio());

        partidasNoEstadio.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(24);
            LocalDateTime inferiorMargin = usedDate.minusHours(24);

            if(inputedDate.isAfter(inferiorMargin) && inputedDate.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Só pode existir uma partida cadastrada por dia em cada estádio.");
            }
        });

    };

}

