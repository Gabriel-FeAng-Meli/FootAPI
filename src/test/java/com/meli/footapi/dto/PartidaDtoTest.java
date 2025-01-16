package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PartidaDtoTest {

    @Test
    void testPartidaToDto() {
        Clube clubeDaCasa = new Clube();
        clubeDaCasa.setNome("casa");
        clubeDaCasa.setEstado("SP");

        Clube clubeVisitante = new Clube();
        clubeVisitante.setNome("visitante");
        clubeVisitante.setEstado("SP");
        
        Estadio estadio = new Estadio();
        estadio.setNome("estadio");
        
        Partida partida = new Partida();
        partida.setId(0);
        partida.setClubeDaCasa(clubeDaCasa);
        partida.setGolsClubeDaCasa(0);
        partida.setClubeVisitante(clubeVisitante);
        partida.setGolsClubeVisitante(0);
        partida.setEstadio(estadio);
        partida.setGoleada(false);

        PartidaDto result = PartidaDto.partidaToDto(partida);
        assertEquals(0, result.getId());
        assertEquals("casa - SP", result.getClubeDaCasaNome());
        assertEquals(0, result.getGolsClubeDaCasa());
        assertEquals("visitante - SP", result.getClubeVisitanteNome());
        assertEquals(0, result.getGolsClubeVisitante());
        assertEquals("estadio", result.getEstadioNome());
        assertFalse(result.isGoleada());
    }
}
