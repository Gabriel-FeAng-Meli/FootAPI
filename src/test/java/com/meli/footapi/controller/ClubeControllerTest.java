package com.meli.footapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.service.ClubeService;
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
class ClubeControllerTest {

        @Autowired
        private MockMvcTester mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private ClubeService clubeService;

        @InjectMocks
        private ClubeController clubeController;

        private Clube clube = new Clube(1, "clube", "SP", true, LocalDate.of(1212, 1, 1));
        ClubeDto clubeDto = ClubeDto.clubeToDto(clube);

        @Test
        void testCreateClub() throws Exception {

                when(clubeService.createClub(clube)).thenReturn(clubeDto);

                MockHttpServletResponse response = mockMvc.perform(post("/clubes").content(objectMapper.writeValueAsString(clube)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(clubeDto);
                String responseContent = response.getContentAsString();

                System.out.println(responseContent);

                assertEquals(201, response.getStatus());
                assertEquals(expectedResponse, responseContent);

        }

        @Test
        void testGetClubById() throws Exception {

                when(clubeService.getClubeById(1)).thenReturn(clubeDto);

                MockHttpServletResponse response = mockMvc.perform(get("/clubes/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(clubeDto);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testGetPaginatedClubes() throws Exception {

                Map<String, Object> expected = new HashMap<>();
                expected.put("Clubes", List.of(clube));
                expected.put("paginaAtual", 1);
                expected.put("totalDeItens", 1);
                expected.put("totalDePaginas", 1);
    

                when(clubeService.getPaginatedClubs(null, "clube", 0, 1)).thenReturn(expected);

                MockHttpServletResponse response = mockMvc.perform(get("/clubes").queryParam("nome", "clube").queryParam("page", "0").queryParam("size", "1").accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(expected);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testUpdateClub() throws Exception {

                Clube novosDados = new Clube(1, "Novo", "RJ", true, LocalDate.of(2000, 1, 1));

                when(clubeService.updateClube(1, novosDados)).thenReturn(ClubeDto.clubeToDto(novosDados));

                MockHttpServletResponse response = mockMvc.perform(put("/clubes/{id}", 1).content(objectMapper.writeValueAsString(novosDados)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).getResponse();

                String expectedResponse = objectMapper.writeValueAsString(novosDados);
                String responseContent = response.getContentAsString();

                assertEquals(expectedResponse, responseContent);
                assertEquals(200, response.getStatus());

        }

        @Test
        void testDeleteClub() throws Exception {

                MockHttpServletResponse response = mockMvc.perform(delete("/clubes/{id}", 1).accept(MediaType.APPLICATION_JSON)).getResponse();

                assertEquals(204, response.getStatus());

        }

}
