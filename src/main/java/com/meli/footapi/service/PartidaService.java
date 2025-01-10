package com.meli.footapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.PartidaRepository;


@Service
public class PartidaService {

    @Autowired
    private PartidaRepository matchRepo;

    @Autowired
    private ClubeService clubService;

    public PartidaDto createMatch(Partida partida) {
        validateMatchInput(partida);

        try {
            matchRepo.save(partida);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel criar a partida. Devem ser fornecidos dados validos de dois clubes existentes e ativos, alem de um estadio existente e uma data válida para criar uma partida.");
        }

        return PartidaDto.partidaToDto(partida);
    }

    public List<PartidaDto> getMatchs() {
        List<PartidaDto> dtoList = new ArrayList<>();
        List<Partida> matchList = matchRepo.findAll();
    
        for (int i = 0; i < matchList.size(); i++) {
            Partida c = matchList.get(i);
            dtoList.add(PartidaDto.partidaToDto(c));
        }

        return dtoList;
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

        matchRepo.save(partida);

        return PartidaDto.partidaToDto(partida);
    }

    public void deleteMatch(int id) {
        Partida matchToDelete = matchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrada nenhuma partida com o id " + id));

        matchRepo.delete(matchToDelete);

    }

    private void validateMatchInput(Partida partidaParaValidar) throws ResponseStatusException {
        // fazer validações que dão throw se der errado
        LocalDateTime inputedDate = partidaParaValidar.getDataPartida();
        
        Clube clubeDaCasaInput = partidaParaValidar.getClubeDaCasa();

        Clube clubeDaCasa = ClubeDto.dtoToClube(clubService.getClubById(clubeDaCasaInput.getId()));
        LocalDateTime dataCriaçãoClubeDaCasa = clubeDaCasa.getDataDeCriacao().atStartOfDay();
        List<Partida> partidasClubeDaCasa = matchRepo.findByClubeDaCasa(clubeDaCasaInput);

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

