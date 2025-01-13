package com.meli.footapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RankingDto {

    private int rank;
    private ClubeDto clube;
    private int pontuacao;
}
