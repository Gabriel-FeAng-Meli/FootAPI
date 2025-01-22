package com.meli.footapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.footapi.dto.PartidaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.PartidaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class PartidaControllerTest {

        @Autowired
        private MockMvcTester mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PartidaService partidaService;

        @InjectMocks
        private PartidaController partidaController;

        private Clube casa = new Clube(1, "casa", "SP", true, LocalDate.of(1212, 1, 1));
        private Clube visitante = new Clube(1, "visitante", "SP", true, LocalDate.of(1212, 1, 1));

        private Estadio estadio = new Estadio(1, "estadio", casa);

        private Partida partida = new Partida(1, casa, 0, visitante, 0, LocalDateTime.of(2012, 1,1,1,1,1), estadio, false);
        private PartidaDto partidaDto = PartidaDto.partidaToDto(partida);

        @Test
        void testCreatePartida() throws Exception {

                when(partidaService.createPartida(partida)).thenReturn(partidaDto);

                MockHttpServletResponse response = mockMvc.perform(post("/partidas").content(objectMapper.writeValueAsString(partida)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(partidaDto);
                String responseContent = response.getContentAsString();

                System.out.println(responseContent);

                assertEquals(201, response.getStatus());
                assertEquals(expectedResponse, responseContent);

        }

        @Test
        void testGetMatchById() throws Exception {

                when(partidaService.getMatchById(1)).thenReturn(partidaDto);

                MockHttpServletResponse response = mockMvc.perform(get("/partidas/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(partidaDto);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testPaginarPartidas() throws Exception {

                Map<String, Object> expected = new HashMap<>();
                expected.put("Partidas", List.of(partida));
                expected.put("paginaAtual", 1);
                expected.put("totalDeItens", 1);
                expected.put("totalDePaginas", 1);
    

                when(partidaService.paginarPartidas(null, null, null, 0, 5)).thenReturn(expected);

                MockHttpServletResponse response = mockMvc.perform(get("/partidas").accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(expected);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testUpdateMatch() throws Exception {

                Partida novosDados = new Partida(1, casa, 2, visitante, 1, LocalDateTime.of(2010, 1, 1, 1, 1, 1), estadio, false);

                when(partidaService.updateMatch(1, novosDados)).thenReturn(PartidaDto.partidaToDto(novosDados));

                MockHttpServletResponse response = mockMvc.perform(put("/partidas/{id}", 1).content(objectMapper.writeValueAsString(novosDados)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                assertEquals(200, response.getStatus());

        }

        @Test
        void testDeleteMatch() throws Exception {

                MockHttpServletResponse response = mockMvc.perform(delete("/partidas/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                assertEquals(204, response.getStatus());

        }

}
