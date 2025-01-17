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
import com.meli.footapi.repository.ClubeRepository;

@ExtendWith(MockitoExtension.class)
public class ClubeValidationTest {

    @InjectMocks
    private ClubeValidation clubeValidation;

    @Mock
    ClubeRepository clubeRepository;

    @Test
    void testValidateClubInput() {
        Clube clubeValido = new Clube(0, "Supimpas", "SP", true, LocalDate.of(1212,1,1));

        assertDoesNotThrow(() -> clubeValidation.validateClubInput(clubeValido));
    }

    @Test
    void testValidateClubInput_NomeInvalido() {
        Clube clubeValido = new Clube(0, "a", "SP", true, LocalDate.of(1212,1,1));

        assertThrows(ResponseStatusException.class,() -> clubeValidation.validateClubInput(clubeValido));
    }

    @Test
    void testValidateClubInput_EstadoInvalido() {
        Clube clubeValido = new Clube(0, "EstadoInvalido", "MQ", true, LocalDate.of(1212,1,1));

        assertThrows(ResponseStatusException.class,() -> clubeValidation.validateClubInput(clubeValido));
    }

    @Test
    void testValidateClubInput_DataDeCriacaoInvalida() {
        Clube clubeValido = new Clube(0, "DataInvalida", "MQ", true, LocalDate.of(2121,1,1));

        assertThrows(ResponseStatusException.class,() -> clubeValidation.validateClubInput(clubeValido));
    }
}
