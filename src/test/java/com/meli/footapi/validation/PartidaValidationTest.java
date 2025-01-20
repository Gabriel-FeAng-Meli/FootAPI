package com.meli.footapi.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.repository.EstadioRepository;
import com.meli.footapi.repository.PartidaRepository;

@ExtendWith(MockitoExtension.class)
public class PartidaValidationTest {

    @InjectMocks
    private PartidaValidation partidaValidation;

    @Mock
    PartidaRepository mockPartidaRepo;

    @Mock
    ClubeRepository mockClubeRepo;

    @Mock
    EstadioRepository mockEstadioRepo;

    Clube clubeCasa = new Clube(0, "Clube da Casa", "SP", true, LocalDate.of(1212,1,1));
    Clube clubeVisitante = new Clube(1, "Clube Visitante", "SP", true, LocalDate.of(1212,1,1));
    Estadio estadio = new Estadio(0, "Casa", clubeCasa);
    Partida partida = new Partida(0, clubeCasa, 0, clubeVisitante, 0, LocalDateTime.of(2020, 1, 1, 1, 1, 1), estadio, false);
    Optional<Clube> clubeDaCasaOpt = Optional.of(clubeCasa);
    Optional<Clube> clubeVisitanteOpt = Optional.of(clubeVisitante);
    Optional<Estadio> estadioOpt = Optional.of(estadio);

    @Test
    void testValidateMatchInput() {

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        assertDoesNotThrow(() -> partidaValidation.validateMatchInput(partida));
    }

    @Test
    void testValidateMatchInput_ClubeDaCasaNaoEncontrado() {        

    when(mockClubeRepo.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
    HttpStatusCode expectedStatus = HttpStatusCode.valueOf(404);
    HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

    assertEquals(expectedStatus, status);


    }

    @Test
    void testValidateMatchInput_ClubeVisitanteNaoEncontrado() {        

    when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);    
    when(mockClubeRepo.findById(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    HttpStatusCode expectedStatus = HttpStatusCode.valueOf(404);
    HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

    assertEquals(expectedStatus, status);

    }

    @Test
    void testValidateMatchInput_EstadioNaoEncontrado() {        

    when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
    when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
    when(mockEstadioRepo.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    HttpStatusCode expectedStatus = HttpStatusCode.valueOf(404);
    HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

    assertEquals(expectedStatus, status);

    }

    @Test
    void testValidateMatchInput_EstadioSemRelacaoComClubeDaCasa() {

        estadio.setClube(clubeVisitante);

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(400);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }


    @Test
    void testValidateMatchInput_PartidaAntesDaCriacaoDoClubeDaCasa() {
        clubeCasa.setDataDeCriacao(LocalDate.of(2121,1,1));

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(400);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }

    @Test
    void testValidateMatchInput_PartidaAntesDaCriacaoDoClubeVisitante() {
        clubeVisitante.setDataDeCriacao(LocalDate.of(2121,1,1));

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(400);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }

    @Test
    void testValidateMatchInput_PartidaAntesDaCriacaoDeAmbosOsClubes() {

        clubeCasa.setDataDeCriacao(LocalDate.of(2121,1,1));
        clubeVisitante.setDataDeCriacao(LocalDate.of(2121,1,1));

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(400);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }


    @Test
    void testValidateMatchInput_ClubeDaCasaJaTemPartidaProxima() {

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        Partida partidaProximaAntes = new Partida();
        partidaProximaAntes.setDataPartida(LocalDateTime.of(2020,1,1,0,1,1));

        when(mockPartidaRepo.findByClubeDaCasa(clubeCasa)).thenReturn(List.of(partidaProximaAntes));

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(409);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }


    @Test
    void testValidateMatchInput_ClubeVisitanteJaTemPartidaProxima() {

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        Partida partidaProximaAntes = new Partida();
        partidaProximaAntes.setDataPartida(LocalDateTime.of(2020,1,2,0,1,1));

        when(mockPartidaRepo.findByClubeDaCasa(clubeCasa)).thenReturn(List.of());
        when(mockPartidaRepo.findByClubeVisitante(clubeVisitante)).thenReturn(List.of(partidaProximaAntes));

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(409);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }


    @Test
    void testValidateMatchInput_EstadioJaTemPartidaProxima() {

        when(mockClubeRepo.findById(0)).thenReturn(clubeDaCasaOpt);
        when(mockClubeRepo.findById(1)).thenReturn(clubeVisitanteOpt);
        when(mockEstadioRepo.findById(0)).thenReturn(estadioOpt);

        Partida partidaProximaAntes = new Partida();
        partidaProximaAntes.setDataPartida(LocalDateTime.of(2020,1,1,0,1,1));

        when(mockPartidaRepo.findByEstadio(estadio)).thenReturn(List.of(partidaProximaAntes));

        HttpStatusCode expectedStatus = HttpStatusCode.valueOf(409);
        HttpStatusCode status = assertThrows(ResponseStatusException.class, () -> partidaValidation.validateMatchInput(partida)).getStatusCode();

        assertEquals(expectedStatus, status);
    }
}
