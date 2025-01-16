package com.meli.footapi.dto;

import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstadioDtoTest {

    @Test
    void testEstadioToDto() {
        Clube clube = new Clube();
        clube.setId(0);
        clube.setNome("clube");
        
        Estadio estadio = new Estadio(0, "estadio", clube);

        EstadioDto estadioDto = EstadioDto.estadioToDto(estadio);
        assertEquals(0, estadioDto.getId());
        assertEquals("estadio", estadioDto.getNome());
        assertEquals(clube, estadioDto.getClube());
    }

    @Test
    void testDtoToEstadio() {
Clube clube = new Clube();
        clube.setId(0);
        clube.setNome("clube");

        EstadioDto dto = new EstadioDto(0, "estadio", clube);

        Estadio expectedResult = new Estadio(0, "estadio", clube);

        Estadio result = EstadioDto.dtoToEstadio(dto);

        assertEquals(expectedResult, result);
    }
}
