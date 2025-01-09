package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.MatchRepo;


@Service
public class MatchService {

    @Autowired
    private MatchRepo matchRepo;

    public PartidaDto createMatch(PartidaDto matchDto) {
        validateMatchInput(matchDto);

        Partida match = PartidaDto.dtoToPartida(matchDto);
        matchRepo.save(match);

        return matchDto;
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

    public PartidaDto updateMatch(int id, PartidaDto updatedMatchInfo) {
        PartidaDto matchToBeUpdated = getMatchById(id);

        validateMatchInput(updatedMatchInfo);

        matchToBeUpdated.setId(id);


        Partida updatedMatch = PartidaDto.dtoToPartida(matchToBeUpdated);
        matchRepo.save(updatedMatch);

        return matchToBeUpdated;
    }

    public void deleteMatch(int id) {
        Partida matchToDelete = matchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrada nenhuma partida com o id " + id));

        matchRepo.delete(matchToDelete);

    }


    private void validateMatchInput(PartidaDto matchToValidate) {
        // fazer validações que dão throw se der errado
    };

}

