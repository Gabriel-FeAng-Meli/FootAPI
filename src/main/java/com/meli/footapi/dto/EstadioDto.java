package com.meli.footapi.dto;

import org.modelmapper.ModelMapper;

import com.meli.footapi.entity.Estadio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EstadioDto {
    private int id;
    private String name;

    public static Estadio dtoToEstadio(EstadioDto dto) {
        ModelMapper mapper = new ModelMapper();
        
        Estadio estadio = mapper.map(dto, Estadio.class);

        return estadio;
    }


    public static EstadioDto estadioToDto(Estadio estadio) {
        ModelMapper mapper = new ModelMapper();

        EstadioDto estadioDto = mapper.map(estadio, EstadioDto.class);

        return estadioDto;
    }
}
