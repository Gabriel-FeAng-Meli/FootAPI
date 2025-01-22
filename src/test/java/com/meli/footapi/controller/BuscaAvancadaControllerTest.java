package com.meli.footapi.controller;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.service.BuscaAvancadaService;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class BuscaAvancadaControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BuscaAvancadaService buscaAvancadaService;

    @InjectMocks
    private BuscaAvancadaController buscaAvancadaController;

    private Clube clube = new Clube(1, "clube", "SP", true, LocalDate.of(1212, 1, 1));
    private Clube outroClube = new Clube(2, "outro", "SP", true, LocalDate.of(1212, 1, 1));

    private Estadio estadio = new Estadio(1, "estadio", clube);

    private Partida partida = new Partida(1, clube, 0, outroClube, 0, LocalDateTime.of(2000, 1, 1, 1, 1, 1), estadio,
            false);

    private List<Partida> partidas = List.of(partida);

    private RetrospectivaDto retro = new RetrospectivaDto(clube, partidas);

    @Test
    void testGetRetrospectiva() throws Exception {

        when(buscaAvancadaService.getRetrospectiva(1)).thenReturn(retro);

        MockHttpServletResponse response = mockMvc
                .perform(get("/busca/{id}/retrospectiva", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

        String expectedResponse = objectMapper.writeValueAsString(retro);
        String responseContent = response.getContentAsString();

        System.out.println(responseContent);

        assertEquals(200, response.getStatus());
        assertEquals(expectedResponse, responseContent);

    }

    @Test
    void testGetConfrontosDiretos() throws Exception {

        retro.setTitulo("clube-SP X outro-SP");

        when(buscaAvancadaService.getConfrontosDiretos(1, 2)).thenReturn(retro);

        MockHttpServletResponse response = mockMvc.perform(get("/busca/{id1}/retrospectiva/{id2}", 1, 2).accept(MediaType.APPLICATION_JSON)).getResponse();

        String expectedResponse = objectMapper.writeValueAsString(retro);
        String responseContent = response.getContentAsString();

        assertEquals(expectedResponse, responseContent);
        assertEquals(200, response.getStatus());

    }

    @Test
    void testGetRetrospectivaParaCadaAdversario() throws Exception {

        retro.setTitulo("clube-SP X outro-SP");

        List<RetrospectivaDto> listaRetro = List.of(retro);
        
        when(buscaAvancadaService.getRetrospectivaParaCadaAdversario(1)).thenReturn(listaRetro);

        MockHttpServletResponse response = mockMvc.perform(get("/busca/{id}/retrospectiva/clubes", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

        String expectedResponse = objectMapper.writeValueAsString(listaRetro);
        String responseContent = response.getContentAsString();

        assertEquals(expectedResponse, responseContent);
        assertEquals(200, response.getStatus());


    }

    @Test
    void testGetRanking() throws Exception {

        RankingDto ranking = new RankingDto(1, ClubeDto.clubeToDto(clube), 0);

        when(buscaAvancadaService.getRanking()).thenReturn(List.of(ranking));

        MockHttpServletResponse response = mockMvc
                .perform(get("/busca/ranking").accept(MediaType.APPLICATION_JSON)).getResponse();

        String expectedResponse = objectMapper.writeValueAsString(List.of(ranking));
        String responseContent = response.getContentAsString();

        System.out.println(responseContent);

        assertEquals(200, response.getStatus());
        assertEquals(expectedResponse, responseContent);

    }

}
