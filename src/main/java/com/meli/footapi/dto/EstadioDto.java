package com.meli.footapi.dto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EstadioDto {
    private int id;
    private String nome;
    private Clube clube;

    public static EstadioDto estadioToDto(Estadio estadio) {
        EstadioDto estadioDto = new EstadioDto(estadio.getId(), estadio.getNome(), estadio.getClube());

        return estadioDto;
    }

    public static Estadio dtoToEstadio(EstadioDto dto) {
        Estadio estadio = new Estadio(dto.getId(), dto.getNome(), dto.getClube());

        return estadio;
    }
}
