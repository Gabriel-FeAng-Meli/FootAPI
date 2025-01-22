package com.meli.footapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.service.EstadioService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class EstadioControllerTest {

        @Autowired
        private MockMvcTester mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private EstadioService estadioService;

        @InjectMocks
        private EstadioController estadioController;

        private Clube clube = new Clube(1, "clube", "SP", true, LocalDate.of(1212, 1, 1));

        private Estadio estadio = new Estadio(1, "estadio", clube);
        private EstadioDto estadioDto = EstadioDto.estadioToDto(estadio);

        @Test
        void testCreateStadium() throws Exception {

                when(estadioService.createStadium(estadio)).thenReturn(estadioDto);

                MockHttpServletResponse response = mockMvc.perform(post("/estadios").content(objectMapper.writeValueAsString(estadio)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(estadioDto);
                String responseContent = response.getContentAsString();

                System.out.println(responseContent);

                assertEquals(201, response.getStatus());
                assertEquals(expectedResponse, responseContent);

        }

        @Test
        void testGetStadiumById() throws Exception {

                when(estadioService.getStadiumById(1)).thenReturn(estadioDto);

                MockHttpServletResponse response = mockMvc.perform(get("/estadios/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(estadioDto);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testPaginarEstadios() throws Exception {

                Map<String, Object> expected = new HashMap<>();
                expected.put("Estadios", List.of(estadio));
                expected.put("paginaAtual", 1);
                expected.put("totalDeItens", 1);
                expected.put("totalDePaginas", 1);
    

                when(estadioService.paginarEstadios("estadio", 0, 1)).thenReturn(expected);

                MockHttpServletResponse response = mockMvc.perform(get("/estadios").queryParam("nome", "estadio").queryParam("page", "0").queryParam("size", "1").accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(expected);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testUpdateStadium() throws Exception {

                Estadio novosDados = new Estadio(1, "Novo", clube);

                when(estadioService.updateStadium(1, novosDados)).thenReturn(EstadioDto.estadioToDto(novosDados));

                MockHttpServletResponse response = mockMvc.perform(put("/estadios/{id}", 1).content(objectMapper.writeValueAsString(novosDados)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(novosDados);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testDeleteStadium() throws Exception {

                MockHttpServletResponse response = mockMvc.perform(delete("/estadios/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                assertEquals(204, response.getStatus());

        }

}
