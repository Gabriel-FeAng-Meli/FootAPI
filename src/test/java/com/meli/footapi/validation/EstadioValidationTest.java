package com.meli.footapi.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
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

    @Test
    void testValidateStadiumInput() {
        Clube clube = new Clube(0, "ClubeLeste", "SP", true, LocalDate.of(1212,1,1));

        Estadio estadioValido = new Estadio(0, "Estadio da Leste", clube);

        assertDoesNotThrow(() -> estadioValidation.validateStadiumInput(estadioValido));
    }

    @Test
    void testValidateStadiumInput_NomeInvalido() {
        Clube clube = new Clube(0, "ClubeLeste", "SP", true, LocalDate.of(1212,1,1));

        Estadio estadioValido = new Estadio(0, "i", clube);

        assertThrows(ResponseStatusException.class, () -> estadioValidation.validateStadiumInput(estadioValido));
    }

}
