package com.meli.footapi.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.repository.EstadioRepository;

@ExtendWith(MockitoExtension.class)
public class EstadioValidationTest {

    @InjectMocks
    private EstadioValidation estadioValidation;

    @Mock
    EstadioRepository estadioRepository;

    @Mock
    ClubeRepository clubeRepository;

    Clube clube = new Clube(0, "Clube Leste", "SP", true, LocalDate.of(1212,1,1));
    Optional<Clube> clubeOpt = Optional.of(clube);
    
    Estadio estadioValido = new Estadio(0, "Estadio da Leste", clube);

    @Test
    void testValidateStadiumInput() {

        when(clubeRepository.findById(0)).thenReturn(clubeOpt);

        assertDoesNotThrow(() -> estadioValidation.validateStadiumInput(estadioValido));
    }

    @Test
    void testValidateStadiumInput_ClubeNãoEncontrado() {
        when(clubeRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> estadioValidation.validateStadiumInput(estadioValido));
    }


    @Test
    void testValidateStadiumInput_ClubeInativo() {

        clube.setAtivo(false);

        when(clubeRepository.findById(0)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class, () -> estadioValidation.validateStadiumInput(estadioValido));
    }

    @Test
    void testValidateStadiumInput_NomeInvalido() {

        Estadio estadioNomeInvalido = new Estadio(0, "i", clube);

        assertThrows(ResponseStatusException.class, () -> estadioValidation.validateStadiumInput(estadioNomeInvalido));
    }

}
