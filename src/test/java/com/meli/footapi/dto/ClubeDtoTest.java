package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClubeDtoTest {

    @Test
    void testDtoToClube() {
        final ClubeDto dto = new ClubeDto(0, "nome", "estado", true, LocalDate.of(2020, 1, 1));
        final Clube expectedResult = new Clube(0, "nome", "estado", true, LocalDate.of(2020, 1, 1));

        final Clube result = ClubeDto.dtoToClube(dto);

        assertEquals(expectedResult, result);
    }

    @Test
    void testClubeToDto() {
        final Clube clube = new Clube(0, "nome", "estado", true, LocalDate.of(2020, 1, 1));
        final ClubeDto expectedResult = new ClubeDto(0, "nome", "estado", true, LocalDate.of(2020, 1, 1));

        final ClubeDto result = ClubeDto.clubeToDto(clube);
        assertEquals(0, result.getId());
        assertEquals("nome", result.getNome());
        assertEquals("estado", result.getEstado());
        assertTrue(result.isAtivo());
        assertEquals(LocalDate.of(2020, 1, 1), result.getDataDeCriacao());

        assertEquals(expectedResult, result);
    }
}
