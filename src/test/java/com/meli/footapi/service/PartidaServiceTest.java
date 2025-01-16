package com.meli.footapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    Estadio estadio;

    @Mock
    Clube casa;
    
    @Mock
    Clube visitante;
    
    @Mock
    LocalDateTime data;

    @Test
    void testCreatePartida() {

        Partida partidaValida = new Partida(0, casa, 0, visitante, 0, data, estadio, false);

        PartidaDto response = partidaService.createPartida(partidaValida);
        assertNotNull(response);
    }

    @Test
    void testDeleteMatch() {
        int id = 123;
        Partida partidaValida = new Partida(id, casa, 0, visitante, 0, data, estadio, false);

        when(partidaRepository.findById(id)).thenReturn(partidaValida);
        PartidaDto partidaParaDeletar = partidaService.getMatchById(id);
        
        assertNotNull(partidaParaDeletar);
    }

    @Test
    void testGetMatchById() {

        int id = 123;
        Partida partidaValida = new Partida(id, casa, 0, visitante, 0, data, estadio, false);
 
        PartidaDto dtoEsperado = PartidaDto.partidaToDto(partidaValida);   
        when(partidaRepository.findById(id)).thenReturn(partidaValida);
        
        PartidaDto dtoRecebido = partidaService.getMatchById(id);

        assertEquals(dtoEsperado, dtoRecebido);
    }

}
