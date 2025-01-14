package com.meli.footapi.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.repository.EstadioRepository;
import com.meli.footapi.repository.PartidaRepository;

@Service
public class PartidaValidation {

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    EstadioRepository estadioRepository;

    @Autowired
    PartidaRepository partidaRepository;

    public void validateMatchInput(Partida partidaParaValidar) throws ResponseStatusException {
        LocalDateTime dataPartida = partidaParaValidar.getDataPartida();
        Clube clubeDaCasa = clubeRepository.findById(partidaParaValidar.getClubeDaCasa().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube da Casa não encontrado"));
        Clube clubeVisitante = clubeRepository.findById(partidaParaValidar.getClubeDaCasa().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube visitante não encontrado"));
        Estadio estadio = estadioRepository.findById(partidaParaValidar.getEstadio().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estadio não encontrado"));
        
        validateDonoDoEstadio(clubeDaCasa, estadio.getId());
        validateEstadioDate(dataPartida, estadio);
        validateClubeDaCasaDate(dataPartida, clubeDaCasa);
        validateClubeVisitanteDate(dataPartida, clubeVisitante);
    }
    


    private void validateClubeDaCasaDate(LocalDateTime dataEHoraDaPartida, Clube clubeDaCasa) {
        LocalDateTime dataCriaçãoClubeDaCasa = clubeDaCasa.getDataDeCriacao().atStartOfDay();
        List<Partida> partidasClubeDaCasa = partidaRepository.findByClubeDaCasa(clubeDaCasa);

        if(dataCriaçãoClubeDaCasa.isAfter(dataEHoraDaPartida)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A partida só pode ocorrer em uma data posterior à criação de ambos os clubes!");
        }

        partidasClubeDaCasa.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(48);
            LocalDateTime inferiorMargin = usedDate.minusHours(48);

            if(dataEHoraDaPartida.isAfter(inferiorMargin) && dataEHoraDaPartida.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cada clube só pode jogar uma partida a cada 48h. O Clube da Casa já tem uma partida cadastrada em " + usedDate);
            }
        });
    }

    private void validateClubeVisitanteDate(LocalDateTime dataEHoraDaPartida, Clube clubeVisitante) {
        LocalDateTime dataCriaçãoClubeVisitante = clubeVisitante.getDataDeCriacao().atStartOfDay();
        List<Partida> partidasClubeVisitante = partidaRepository.findByClubeVisitante(clubeVisitante);

        partidasClubeVisitante.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(48);
            LocalDateTime inferiorMargin = usedDate.minusHours(48);

            if(dataEHoraDaPartida.isAfter(inferiorMargin) && dataEHoraDaPartida.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cada clube só pode jogar uma partida a cada 48h. O Clube Visitante já tem uma partida cadastrada em " + usedDate);
            }
        });

        if(dataCriaçãoClubeVisitante.isAfter(dataEHoraDaPartida)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A partida só pode ocorrer em uma data posterior à criação de ambos os clubes!");
        }

    }
        

    private void validateEstadioDate(LocalDateTime dataEHoraDaPartida, Estadio estadioDaPartida) {

        List<Partida> partidasNoEstadio = partidaRepository.findByEstadio(estadioDaPartida);

        partidasNoEstadio.forEach(p -> {
            LocalDateTime usedDate = p.getDataPartida();
            LocalDateTime superiorMargin = usedDate.plusHours(24);
            LocalDateTime inferiorMargin = usedDate.minusHours(24);

            if(dataEHoraDaPartida.isAfter(inferiorMargin) && dataEHoraDaPartida.isBefore(superiorMargin)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Só pode existir uma partida cadastrada por dia em cada estádio.");
            }
        });
    }


    private void validateDonoDoEstadio(Clube clubeDaCasa, int idClubeAssociadoAoEstadio) {
        int idClubeDaCasa = clubeDaCasa.getId();
        if (idClubeAssociadoAoEstadio != idClubeDaCasa) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O id do estadio inserido não é do estadio pertencente ao clube " + clubeDaCasa.getNome());
        }
    };

}
