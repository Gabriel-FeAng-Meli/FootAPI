package com.meli.footapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
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
    void testDeleteMatch() {

        when(partidaRepository.findById(0)).thenReturn(partida);

        assertDoesNotThrow(() -> partidaService.deleteMatch(0));
        
    }

    @Test
    void testGetPartidasPaginadasByGoleada() {

        PageRequest paginacao = PageRequest.of(0, 1);

        when(partidaRepository.findByGoleada(true, paginacao)).thenReturn(Page.empty());

        List<Partida> expectedContent = List.of();

        Page<Partida> result = partidaService.getPartidasPaginadasByGoleada(true, 0, 1);

        assertEquals(expectedContent, result.getContent());
    }


    @Test
    void testGetPartidasPaginadas() {

        PageRequest paginacao = PageRequest.of(0, 1);

        when(partidaRepository.findAll(paginacao)).thenReturn(Page.empty());

        List<Partida> expectedContent = List.of();

        Page<Partida> result = partidaService.getPartidasPaginadas(0, 1);

        assertEquals(expectedContent, result.getContent());
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

}
