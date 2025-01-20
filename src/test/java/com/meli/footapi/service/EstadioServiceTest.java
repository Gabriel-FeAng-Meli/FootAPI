package com.meli.footapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.EstadioRepository;
import com.meli.footapi.validation.EstadioValidation;

@ExtendWith(MockitoExtension.class)
public class EstadioServiceTest {

    @InjectMocks
    private EstadioService estadioService;

    @Mock
    private EstadioRepository estadioRepository;

    @Mock
    private EstadioValidation estadioValidation;

    Clube clube = new Clube(0, "casa", "SP", true, LocalDate.of(1212,1,1));

    Estadio estadio = new Estadio(0, "estadio", clube);
    Optional<Estadio> estadioOpt = Optional.of(estadio);


    @Test
    void testCreateStadium() {

        EstadioDto expected = EstadioDto.estadioToDto(estadio);

        EstadioDto response = estadioService.createStadium(estadio);

        assertEquals(expected, response);

    }

    @Test
    void testDeleteStadium() {

        when(estadioRepository.findById(0)).thenReturn(estadioOpt);

        assertDoesNotThrow(() -> estadioService.deleteStadium(0));
        
    }

    @Test
    void testPaginarEstadios() {

        PageRequest paginacao = PageRequest.of(0, 1);

        when(estadioRepository.findAll(paginacao)).thenReturn(Page.empty());

        List<Estadio> expectedContent = List.of();

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("Estadios", expectedContent);
        expectedResult.put("paginaAtual", 1);
        expectedResult.put("totalDeItens", 0);
        expectedResult.put("totalDePaginas", 1);


        Map<String, Object> result = estadioService.paginarEstadios(null, 0, 1);

        assertEquals(expectedResult.get("Estadios"), result.get("Estadios"));
    }


    @Test
    void testGetPaginatedClubs_ByNome() {

        PageRequest paginacao = PageRequest.of(0, 1);

        when(estadioRepository.findByNome("casa" ,paginacao)).thenReturn(Page.empty());

        List<Estadio> expectedContent = List.of();

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("Estadios", expectedContent);
        expectedResult.put("paginaAtual", 1);
        expectedResult.put("totalDeItens", 0);
        expectedResult.put("totalDePaginas", 1);

        Map<String, Object> result = estadioService.paginarEstadios("casa", 0, 1);

        assertEquals(expectedResult.get("Estadios"), result.get("Estadios"));
    }

    @Test
    void testGetStadiumById() {
 
        EstadioDto dtoEsperado = EstadioDto.estadioToDto(estadio);   
        when(estadioRepository.findById(0)).thenReturn(estadioOpt);
        
        EstadioDto dtoRecebido = estadioService.getStadiumById(0);

        assertEquals(dtoEsperado, dtoRecebido);
    }


    @Test
    void testGetStadiumById_NaoEncontrado() {
 
        when(estadioRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        assertThrows(ResponseStatusException.class, () -> estadioService.getStadiumById(0));

    }

}
