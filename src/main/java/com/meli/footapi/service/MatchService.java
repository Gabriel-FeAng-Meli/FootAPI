package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.MatchDto;
import com.meli.footapi.entity.Match;
import com.meli.footapi.repository.MatchRepo;


@Service
public class MatchService {

    @Autowired
    private MatchRepo matchRepo;

    public MatchDto createMatch(MatchDto matchDto) {
        validateMatchInput(matchDto);

        Match match = MatchDto.dtoToMatch(matchDto);
        matchRepo.save(match);

        return matchDto;
    }

    public List<MatchDto> getMatchs() {
        List<MatchDto> dtoList = new ArrayList<>();
        List<Match> matchList = matchRepo.findAll();
    
        for (int i = 0; i < matchList.size(); i++) {
            Match c = matchList.get(i);
            dtoList.add(MatchDto.matchToDto(c));
        }

        return dtoList;
    }

    public MatchDto getMatchById(int id) {

        Match match = this.matchRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrada nenhuma partida com o ID " + id));

        MatchDto dto = MatchDto.matchToDto(match);

        return dto;
    }

    public MatchDto updateMatch(int id, MatchDto updatedMatchInfo) {
        MatchDto matchToBeUpdated = getMatchById(id);

        validateMatchInput(updatedMatchInfo);

        matchToBeUpdated.setId(id);


        Match updatedMatch = MatchDto.dtoToMatch(matchToBeUpdated);
        matchRepo.save(updatedMatch);

        return matchToBeUpdated;
    }

    public void deleteMatch(int id) {
        Match matchToDelete = matchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrada nenhuma partida com o id " + id));

        matchRepo.delete(matchToDelete);

    }


    private void validateMatchInput(MatchDto matchToValidate) {
        // fazer validações que dão throw se der errado
    };

}

