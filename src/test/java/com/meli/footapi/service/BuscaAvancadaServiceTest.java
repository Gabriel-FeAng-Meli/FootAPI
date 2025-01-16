package com.meli.footapi.service;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaAvancadaServiceTest {
    
    @InjectMocks
    private BuscaAvancadaService buscaAvancadaService;

    @Mock
    private ClubeService mockClubeService;

    @Mock
    private PartidaService mockPartidaService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRetrospectiva() {
        RetrospectivaDto expectedResult = new RetrospectivaDto("clube-SP", 0,0,0,1,0,1,1);

        ClubeDto clubeDto = new ClubeDto(0, "clube", "SP", true, LocalDate.of(1212, 1, 1));

        when(mockClubeService.getClubeById(0)).thenReturn(clubeDto);

        Clube clubeDaCasa = new Clube(0, "clube", "SP", true, LocalDate.of(1212,1,1));

        Clube clubeVisitante = new Clube(1, "clubeAdversario", "SP", true, LocalDate.of(1212,1,1));
        
        Partida partida = new Partida();
        partida.setClubeDaCasa(clubeDaCasa);
        partida.setClubeVisitante(clubeVisitante);
        
        List<Partida> partidas = List.of(partida);

        when(mockPartidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(partidas);

        RetrospectivaDto result = buscaAvancadaService.getRetrospectiva(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectiva_MasClubeSemPartidas() {

        ClubeDto clubeDto = new ClubeDto(0, "clubeSemPartidas", "SP", true, LocalDate.of(1212, 1, 1));
        
        RetrospectivaDto expectedResult = new RetrospectivaDto("clubeSemPartidas-SP", 0, 0, 0, 
        0, 0, 0, 0);

        when(mockClubeService.getClubeById(0)).thenReturn(clubeDto);

        when(mockPartidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        RetrospectivaDto result = buscaAvancadaService.getRetrospectiva(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetConfrontosDiretos_MasClubesNuncaSeEnfrentaram() {
        RetrospectivaDto expectedResult = new RetrospectivaDto("clubeSemPartidas-SP X outroClube-SP", 0, 0, 0, 
        0, 0, 0, 0);

        ClubeDto clubeDto = new ClubeDto(0, "clubeSemPartidas", "SP", true, LocalDate.of(1212, 1, 1));


        when(mockClubeService.getClubeById(0)).thenReturn(clubeDto);

        ClubeDto outroClubeDto = new ClubeDto(1, "outroClube", "SP", true, LocalDate.of(1212, 1, 1));

        when(mockClubeService.getClubeById(1)).thenReturn(outroClubeDto);

        when(mockPartidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        RetrospectivaDto result = buscaAvancadaService.getConfrontosDiretos(0, 1);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectivaParaCadaAdversario() {
        RetrospectivaDto retrospectivaDto = new RetrospectivaDto("casa-SP X visitante-SP",
         0, 0, 0, 1, 0, 1, 1);
        List<RetrospectivaDto> expectedResult = List.of(retrospectivaDto);

        ClubeDto clubeDto = new ClubeDto(0, "casa", "SP", true, LocalDate.of(1212, 1, 1));
        when(mockClubeService.getClubeById(0)).thenReturn(clubeDto);


        ClubeDto outroClubeDto = new ClubeDto(1, "visitante", "SP", true, LocalDate.of(1212, 1, 1));
        when(mockClubeService.getClubeById(1)).thenReturn(outroClubeDto);

        Clube clubeDaCasa = ClubeDto.dtoToClube(clubeDto);
        Clube clubeVisitante = ClubeDto.dtoToClube(outroClubeDto);

        Partida partida = new Partida();
        partida.setClubeDaCasa(clubeDaCasa);
        partida.setClubeVisitante(clubeVisitante);

        List<Partida> partidas = List.of(partida);

        when(mockPartidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(partidas);

        List<RetrospectivaDto> result = buscaAvancadaService.getRetrospectivaParaCadaAdversario(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRetrospectivaParaCadaAdversario_MasClubeSemPartidas() {
        ClubeDto clubeDto = new ClubeDto(0, "clube", "SP", true, LocalDate.of(1212, 1, 1));
        when(mockClubeService.getClubeById(0)).thenReturn(clubeDto);

        when(mockPartidaService.getPartidasByClube(ClubeDto.dtoToClube(clubeDto))).thenReturn(List.of());

        List<RetrospectivaDto> result = buscaAvancadaService.getRetrospectivaParaCadaAdversario(0);

        assertEquals(List.of(), result);
    }

    @Test
    void testGetRanking() {}

}
