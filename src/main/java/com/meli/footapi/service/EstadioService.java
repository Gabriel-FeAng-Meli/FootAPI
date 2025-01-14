package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.EstadioRepository;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository stadiumRepo;

    @Autowired
    private ClubeService clubeService;

    public EstadioDto createStadium(Estadio stadium) {
        validateStadiumInput(stadium);

        stadiumRepo.save(stadium);

        return getStadiumById(stadium.getId());
    }

    public List<EstadioDto> getStadiums() {
        List<EstadioDto> dtoList = new ArrayList<>();
        List<Estadio> stadiumList = stadiumRepo.findAll();
    
        for (int i = 0; i < stadiumList.size(); i++) {
            Estadio c = stadiumList.get(i);
            dtoList.add(EstadioDto.estadioToDto(c));
        }

        return dtoList;
    }

    public Page<Estadio> findByNome(String nome, int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Estadio> paginado = stadiumRepo.findByNome(nome, paginacao);

        return paginado;
    } 

    public Page<Estadio> findAll(int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Estadio> paginado = stadiumRepo.findAll(paginacao);

        return paginado;
    } 


    public EstadioDto getStadiumById(int id) {

        Estadio stadium = this.stadiumRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum estádio com o ID " + id));

        EstadioDto dto = EstadioDto.estadioToDto(stadium);

        return dto;
    }

    public EstadioDto updateStadium(int id, Estadio updatedStadiumInfo) {
        Estadio estadio = stadiumRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estadio não encontrado para realizar a alteração. Utilize um id de um estadio já cadastrado para realizar uma alteração."));

        estadio.setId(id);
        estadio.setNome(updatedStadiumInfo.getNome());
        estadio.setClube(updatedStadiumInfo.getClube());

        stadiumRepo.save(estadio);

        EstadioDto dto = EstadioDto.estadioToDto(estadio);

        return dto;
    }

    public void deleteStadium(int id) {
        Estadio stadiumToDelete = stadiumRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum estádio com o ID " + id));

        stadiumRepo.delete(stadiumToDelete);

    }


    private void validateStadiumInput(Estadio estadioParaValidar) {

        Clube clubeAssociado = estadioParaValidar.getClube();
        validateHomeClub(clubeAssociado);

        String inputedName = estadioParaValidar.getNome();
        validateStadiumName(inputedName);

        List<EstadioDto> estadiosExistentes = getStadiums();
        checkIfStadiumAlreadyExists(inputedName, estadiosExistentes);
    }

    private void checkIfStadiumAlreadyExists(String inputedName, List<EstadioDto> estadiosExistentes) {
        estadiosExistentes.stream().forEach(estadio -> {
            if(estadio.getNome().equals(inputedName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um estadio de mesmo nome já está cadastrado.");
            }
        });
    }

    private void validateStadiumName(String inputedName) {
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estadio deve conter no mínimo 3 letras.");
        }
    };

    private void validateHomeClub(Clube clubeAssociado) {
        try {
            clubeService.getClubById(clubeAssociado.getId());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estadio precisa estar vinculado a um clube existente e ativo.");
        }
    }

}

