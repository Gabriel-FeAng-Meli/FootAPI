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

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.validation.ClubeValidation;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    @InjectMocks
    private ClubeService clubeService;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private ClubeValidation clubeValidation;

    Clube clube = new Clube(0, "casa", "SP", true, LocalDate.of(1212,1,1));
    Optional<Clube> clubeOpt = Optional.of(clube);

    Clube outroClube = new Clube(1, "visitante", "SP", true, LocalDate.of(1212,1,1));

    Estadio estadio = new Estadio(0, "estadio", clube);

    @Test
    void testCreateClub() {

        ClubeDto expected = ClubeDto.clubeToDto(clube);

        ClubeDto response = clubeService.createClub(clube);

        assertEquals(expected, response);

    }

    @Test
    void testGetPaginatedClubs_TodosOsClubes() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("Clubes", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        Pageable paginacao = PageRequest.of(0, 1);

        when(clubeRepository.findAll(paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = clubeService.getPaginatedClubs(null, null, 0, 1);

        assertEquals(expected.get("Clubes"), response.get("Clubes"));
    }

    @Test
    void testGetPaginatedClubs_ByNome() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("Clubes", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        PageRequest paginacao = PageRequest.of(0, 1);

        when(clubeRepository.findByNomeContaining("casa", paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = clubeService.getPaginatedClubs(null, "casa", 0, 1);

        assertEquals(expected.get("Clubes"), response.get("Clubes"));

    }

    @Test
    void testGetPaginatedClubs_ByActivity() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("Clubes", List.of());
        expected.put("paginaAtual", 1);
        expected.put("totalDeItens", 0);
        expected.put("totalDePaginas", 1);

        PageRequest paginacao = PageRequest.of(0, 1);

        when(clubeRepository.findByAtivo(true, paginacao)).thenReturn(Page.empty());

        Map<String, Object> response = clubeService.getPaginatedClubs(true, null, 0, 1);

        assertEquals(expected.get("Clubes"), response.get("Clubes"));

    }

    @Test
    void testGetPaginatedClubs_BadRequest() {
        assertThrows(ResponseStatusException.class, () -> clubeService.getPaginatedClubs(null, "casa", 0, 0));

    }


    @Test
    void testDeleteClube() {

        when(clubeRepository.findById(0)).thenReturn(clubeOpt);

        assertDoesNotThrow(() -> clubeService.deleteClube(0));

    }

    @Test
    void testDeleteClube_ClubeNaoEncontrado() {

        when(clubeRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> clubeService.deleteClube(0));

    }

    @Test
    void testUpdateClube() {
        Clube novosDados = new Clube(0, "novo nome", "RJ", true, LocalDate.of(1212, 4, 4));

        int idDoClubeASerAtualizado = 0;

        when(clubeRepository.findById(0)).thenReturn(clubeOpt);

        ClubeDto expected = ClubeDto.clubeToDto(novosDados);

        ClubeDto result = clubeService.updateClube(idDoClubeASerAtualizado, novosDados);

        assertEquals(expected, result);

    }

    @Test
    void testUpdateClube_ClubeNaoEncontrado() {
        Clube novosDados = new Clube(0, "novo nome", "RJ", true, LocalDate.of(1212, 4, 4));

        int idDoClubeASerAtualizado = 0;

        when(clubeRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> clubeService.updateClube(idDoClubeASerAtualizado, novosDados));

    }

    @Test
    void testGetClubeById() {

        ClubeDto dtoEsperado = ClubeDto.clubeToDto(clube);
        when(clubeRepository.findById(0)).thenReturn(clubeOpt);

        ClubeDto dtoRecebido = clubeService.getClubeById(0);

        assertEquals(dtoEsperado, dtoRecebido);
    }


    @Test
    void testGetClubeById_NaoEncontrada() {

        when(clubeRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> clubeService.getClubeById(0));

    }

    @Test
    void testListarClubesDto() {
        List<ClubeDto> expected = List.of(ClubeDto.clubeToDto(clube));

        when(clubeRepository.findByAtivo(true)).thenReturn(List.of(clube));

        List<ClubeDto> response = clubeService.listarClubesAtivos();

        assertEquals(expected, response);
    }
}
