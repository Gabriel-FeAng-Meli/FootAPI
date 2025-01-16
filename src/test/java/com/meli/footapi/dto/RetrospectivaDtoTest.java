package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RetrospectivaDtoTest {

    @Test
    void testGetTimesEnfrentadosPorClube() {
        Clube clube = new Clube(0, "casa", "SP", true, LocalDate.of(1212, 1, 1));

        Clube clubeDaCasa = new Clube();
        clubeDaCasa.setId(0);
        clubeDaCasa.setNome("casa");
        clubeDaCasa.setEstado("SP");

        Clube clubeVisitante = new Clube();
        clubeVisitante.setId(1);
        clubeVisitante.setNome("visitante");
        clubeVisitante.setEstado("SP");
        
        Partida partida = new Partida();
        partida.setClubeDaCasa(clubeDaCasa);
        partida.setGolsClubeDaCasa(0);
        partida.setGolsClubeVisitante(0);
        partida.setClubeVisitante(clubeVisitante);
        
        List<Partida> partidasJogadas = List.of(partida);

        Set<Integer> expectedResult = Set.of(1);
        Set<Integer> result = RetrospectivaDto.getTimesEnfrentadosPorClube(clube, partidasJogadas);

        assertEquals(expectedResult, result);
    }
}
