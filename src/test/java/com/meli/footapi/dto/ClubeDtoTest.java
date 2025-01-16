package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClubeDtoTest {

    @Test
    void testDtoToClube() {
        ClubeDto dto = new ClubeDto(0, "nome", "SP", true, LocalDate.of(1212, 1, 1));
        Clube expectedResult = new Clube(0, "nome", "SP", true, LocalDate.of(1212, 1, 1));

        Clube result = ClubeDto.dtoToClube(dto);

        assertEquals(expectedResult, result);
    }

    @Test
    void testClubeToDto() {
        Clube clube = new Clube(0, "nome", "SP", true, LocalDate.of(1212, 1, 1));
        
        ClubeDto expectedResult = new ClubeDto(0, "nome", "SP", true, LocalDate.of(1212, 1, 1));
        ClubeDto result = ClubeDto.clubeToDto(clube);

        assertEquals(0, result.getId());
        assertEquals("nome", result.getNome());
        assertEquals("SP", result.getEstado());
        assertTrue(result.isAtivo());
        assertEquals(LocalDate.of(1212, 1, 1), result.getDataDeCriacao());

        assertEquals(expectedResult, result);
    }
}
