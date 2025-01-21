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
import org.springframework.data.domain.Pageable;
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


    Clube clube = new Clube(0, "Clube", "SP", true, LocalDate.of(1212, 1, 1));
    Optional<Clube> clubeOpt = Optional.of(clube);

    Estadio estadio = new Estadio(0, "estadio", clube);
    Optional<Estadio> estadioOpt = Optional.of(estadio);

    @Test
    void testCreateStadium() {

        EstadioDto expected = EstadioDto.estadioToDto(estadio);

        EstadioDto response = estadioService.createStadium(estadio);

        assertEquals(expected, response);

    }

    @Test
    void testGetPaginatedClubs_TodosOsEstadios() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("Estadios", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        Pageable paginacao = PageRequest.of(0, 1);

        when(estadioRepository.findAll(paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = estadioService.paginarEstadios(null, 0, 1);

        assertEquals(expected.get("Estadios"), response.get("Estadios"));
    }

    @Test
    void testGetPaginatedClubs_ByNome() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("Estadios", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        PageRequest paginacao = PageRequest.of(0, 1);

        when(estadioRepository.findByNome("estadio" ,paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = estadioService.paginarEstadios("estadio", 0, 1);

        assertEquals(expected.get("Estadios"), response.get("Estadios"));

    }

    @Test
    void testGetPaginatedClubs_BadRequest() {
        assertThrows(ResponseStatusException.class, () -> estadioService.paginarEstadios("estadio", 0, 0));

    }


    @Test
    void testDeleteEstadio() {

        when(estadioRepository.findById(0)).thenReturn(estadioOpt);

        assertDoesNotThrow(() -> estadioService.deleteStadium(0));
        
    }

    @Test
    void testDeleteEstadio_EstadioNaoEncontrado() {

        when(estadioRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> estadioService.deleteStadium(0));
        
    }

    @Test
    void testUpdateEstadio() {
        Estadio novosDados = new Estadio(0, "novo nome", clube);

        int idDoEstadioASerAtualizado = 0;

        when(estadioRepository.findById(0)).thenReturn(estadioOpt);

        EstadioDto expected = EstadioDto.estadioToDto(novosDados);

        EstadioDto result = estadioService.updateStadium(idDoEstadioASerAtualizado, novosDados);
        
        assertEquals(expected, result);

    }

    @Test
    void testUpdateEstadio_EstadioNaoEncontrado() {
        Estadio novosDados = new Estadio(0, "novo nome", clube);
        
        int idDoEstadioASerAtualizado = 0;

        when(estadioRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> estadioService.updateStadium(idDoEstadioASerAtualizado, novosDados));
        
    }

    @Test
    void testGetEstadioById() {
 
        EstadioDto dtoEsperado = EstadioDto.estadioToDto(estadio);   
        when(estadioRepository.findById(0)).thenReturn(estadioOpt);
        
        EstadioDto dtoRecebido = estadioService.getStadiumById(0);

        assertEquals(dtoEsperado, dtoRecebido);
    }


    @Test
    void testGetEstadioById_NaoEncontrada() {
 
        when(estadioRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        assertThrows(ResponseStatusException.class, () -> estadioService.getStadiumById(0));

    }

    @Test
    void testListarEstadiosDto() {
        List<EstadioDto> expected = List.of(EstadioDto.estadioToDto(estadio));

        when(estadioRepository.findAll()).thenReturn(List.of(estadio));

        List<EstadioDto> response = estadioService.listarEstadiosDto();

        assertEquals(expected, response);
    }

}
