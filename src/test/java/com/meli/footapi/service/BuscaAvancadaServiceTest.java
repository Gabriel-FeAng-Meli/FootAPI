package com.meli.footapi.service;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaAvancadaServiceTest {
    
    @InjectMocks
    private BuscaAvancadaService buscaAvancadaService;

    @Mock
    private ClubeService clubeService;

    @Mock
    private PartidaService partidaService;

    private ClubeDto clubeDto = new ClubeDto(0, "clube", "SP", true, LocalDate.of(1212, 1, 1));
    private ClubeDto outroClubeDto = new ClubeDto(1, "outroClube", "SP", true, LocalDate.of(1212, 1, 1));
    private Clube clubeDaCasa = new Clube(0, "clube", "SP", true, LocalDate.of(1212,1,1));
    private Clube clubeVisitante = new Clube(1, "clubeAdversario", "SP", true, LocalDate.of(1212,1,1));
    private Partida partida = new Partida(0, clubeDaCasa, 0, clubeVisitante, 0, LocalDateTime.of(2000, 1, 1, 1, 1, 1), null, false);
    private List<Partida> partidas = List.of(partida);

    @Test
    void testGetRetrospectiva() {
        RetrospectivaDto expectedResult = new RetrospectivaDto("clube-SP", 0,0,0,1,0,1,1);

        when(clubeService.getClubeById(0)).thenReturn(clubeDto);

        when(partidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(partidas);

        RetrospectivaDto result = buscaAvancadaService.getRetrospectiva(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectiva_MasClubeSemPartidas() {

        RetrospectivaDto expectedResult = new RetrospectivaDto("clube-SP", 0, 0, 0, 
        0, 0, 0, 0);

        when(clubeService.getClubeById(0)).thenReturn(clubeDto);

        when(partidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        RetrospectivaDto result = buscaAvancadaService.getRetrospectiva(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetConfrontosDiretos_MasClubesNuncaSeEnfrentaram() {
        RetrospectivaDto expectedResult = new RetrospectivaDto("clube-SP X outroClube-SP", 0, 0, 0, 
        0, 0, 0, 0);

        when(clubeService.getClubeById(0)).thenReturn(clubeDto);

        when(clubeService.getClubeById(1)).thenReturn(outroClubeDto);

        when(partidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        RetrospectivaDto result = buscaAvancadaService.getConfrontosDiretos(0, 1);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectivaParaCadaAdversario() {
        RetrospectivaDto retrospectivaDto = new RetrospectivaDto("casa-SP X visitante-SP",
         0, 0, 0, 1, 0, 1, 1);
        List<RetrospectivaDto> expectedResult = List.of(retrospectivaDto);

        ClubeDto clubeDto = new ClubeDto(0, "casa", "SP", true, LocalDate.of(1212, 1, 1));
        when(clubeService.getClubeById(0)).thenReturn(clubeDto);

        ClubeDto outroClubeDto = new ClubeDto(1, "visitante", "SP", true, LocalDate.of(1212, 1, 1));
        when(clubeService.getClubeById(1)).thenReturn(outroClubeDto);

        Clube clubeDaCasa = ClubeDto.dtoToClube(clubeDto);
        Clube clubeVisitante = ClubeDto.dtoToClube(outroClubeDto);

        Partida partida = new Partida();
        partida.setClubeDaCasa(clubeDaCasa);
        partida.setClubeVisitante(clubeVisitante);

        List<Partida> partidas = List.of(partida);

        when(partidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(partidas);

        List<RetrospectivaDto> result = buscaAvancadaService.getRetrospectivaParaCadaAdversario(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectivaParaCadaAdversario_MasClubeSemPartidas() {
        when(clubeService.getClubeById(0)).thenReturn(clubeDto);

        when(partidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        List<RetrospectivaDto> result = buscaAvancadaService.getRetrospectivaParaCadaAdversario(0);

        assertEquals(List.of(), result);
    }

    @Test
    void testGetRanking() {

        ClubeDto casa = ClubeDto.clubeToDto(clubeDaCasa);
        ClubeDto visitante = ClubeDto.clubeToDto(clubeVisitante);

        RankingDto expectedRank1 = new RankingDto(1, casa, 1);
        RankingDto expectedRank2 = new RankingDto(2, visitante, 1);

        when(clubeService.listarClubesAtivos()).thenReturn(List.of(casa, visitante));
        when(clubeService.getClubeById(0)).thenReturn(casa);
        when(clubeService.getClubeById(1)).thenReturn(visitante);
        when(partidaService.getPartidasByClube(clubeDaCasa)).thenReturn(partidas);
        when(partidaService.getPartidasByClube(clubeVisitante)).thenReturn(partidas);

        assertEquals(List.of(expectedRank1, expectedRank2), buscaAvancadaService.getRanking());
    }

    @Test
    void testGetRanking_SemClubesCadastrados() {
        when(clubeService.listarClubesAtivos()).thenReturn(List.of());

        assertEquals(List.of(), buscaAvancadaService.getRanking());
    }
}
