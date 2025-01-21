package com.meli.footapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.PartidaRepository;
import com.meli.footapi.validation.PartidaValidation;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceTest {

    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private PartidaValidation partidaValidation;

    Clube clubeDaCasa = new Clube(0, "casa", "SP", true, LocalDate.of(1212,1,1));
    Clube clubeVisitante = new Clube(1, "visitante", "SP", true, LocalDate.of(1212,1,1));
    Estadio estadio = new Estadio(0, "estadio", clubeDaCasa);
    Partida partida = new Partida(0, clubeDaCasa, 0, clubeVisitante, 0, LocalDateTime.of(1212, 1, 1, 1, 1, 1), estadio, false);

    @Test
    void testCreatePartida() {

        PartidaDto expected = PartidaDto.partidaToDto(partida);

        PartidaDto response = partidaService.createPartida(partida);

        assertEquals(expected, response);

    }

    @Test
    void testPaginarPartidas() {

        Pageable paginacao = PageRequest.of(0, 1);

        Map<String, Object> expected = new HashMap<>();
        expected.put("Partidas", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        when(partidaRepository.findAll(paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = partidaService.paginarPartidas(null, null, null, 0, 1);

        assertEquals(expected.get("Partidas"), response.get("Partidas"));
    }

    @Test
    void testPaginarPartidas_InvalidInput() {

        assertThrows(ResponseStatusException.class, () -> partidaService.paginarPartidas(null, null, null, 0, 0));
    }

    @Test
    void testPaginarPartidas_ByGoleada() {

        Pageable paginacao = PageRequest.of(0, 1);

        Map<String, Object> expected = new HashMap<>();
        expected.put("Partidas", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        when(partidaRepository.findByGoleada(false, paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = partidaService.paginarPartidas(false, null, null, 0, 1);

        assertEquals(expected.get("Partidas"), response.get("Partidas"));
    }

    @Test
    void testPaginarPartidas_ByClubeDaCasa() {

        Pageable paginacao = PageRequest.of(0, 1);

        Map<String, Object> expected = new HashMap<>();
        expected.put("Partidas", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        when(partidaRepository.findByClubeDaCasaNomeContains("casa", paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = partidaService.paginarPartidas(null, "casa", null, 0, 1);

        assertEquals(expected.get("Partidas"), response.get("Partidas"));
    }

    @Test
    void testPaginarPartidas_ByClubeVisitante() {

        Pageable paginacao = PageRequest.of(0, 1);

        Map<String, Object> expected = new HashMap<>();
        expected.put("Partidas", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        when(partidaRepository.findByClubeVisitanteNomeContains("visitante", paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = partidaService.paginarPartidas(null, null, "visitante", 0, 1);

        assertEquals(expected.get("Partidas"), response.get("Partidas"));
    }

    @Test
    void testDeleteMatch() {

        when(partidaRepository.findById(0)).thenReturn(partida);

        assertDoesNotThrow(() -> partidaService.deleteMatch(0));
        
    }

    @Test
    void testDeleteMatch_PartidaNaoEncontrada() {

        when(partidaRepository.findById(10)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> partidaService.deleteMatch(10));

    }

    @Test
    void testUpdateMatch() {

        Partida novosDados = new Partida(0, clubeDaCasa, 0, clubeVisitante, 0, LocalDateTime.of(1212, 1, 1, 5, 1, 1), estadio, false);

        int idPartidaParaAtualizar = 0;

        when(partidaRepository.findById(0)).thenReturn(partida);

        PartidaDto expected = PartidaDto.partidaToDto(novosDados);

        PartidaDto result = partidaService.updateMatch(idPartidaParaAtualizar, novosDados);
        
        assertEquals(expected, result);

    }

    @Test
    void testUpdateMatch_PartidaNaoEncontrada() {

        Partida novosDados = new Partida(0, clubeDaCasa, 0, clubeVisitante, 0, LocalDateTime.of(1212, 1, 1, 5, 1, 1), estadio, false);

        int idPartidaParaAtualizar = 10;

        when(partidaRepository.findById(10)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        assertThrows(ResponseStatusException.class, () -> partidaService.updateMatch(idPartidaParaAtualizar, novosDados));
    }

    @Test
    void testGetMatchById() {
 
        PartidaDto dtoEsperado = PartidaDto.partidaToDto(partida);   
        when(partidaRepository.findById(0)).thenReturn(partida);
        
        PartidaDto dtoRecebido = partidaService.getMatchById(0);

        assertEquals(dtoEsperado, dtoRecebido);
    }


    @Test
    void testGetMatchById_NaoEncontrada() {
 
        when(partidaRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        assertThrows(ResponseStatusException.class, () -> partidaService.getMatchById(0));

    }

    @Test
    void testGetPartidasEntreDoisClubes() {
        when(partidaRepository.findByClubeDaCasa(clubeDaCasa)).thenReturn(List.of(partida));
        when(partidaRepository.findByClubeVisitante(clubeDaCasa)).thenReturn(List.of());
        when(partidaRepository.findByClubeDaCasa(clubeVisitante)).thenReturn(List.of());
        when(partidaRepository.findByClubeVisitante(clubeVisitante)).thenReturn(List.of(partida));

        assertEquals(List.of(partida), partidaService.getPartidasEntreDoisClubes(clubeDaCasa, clubeVisitante));
    }

}
