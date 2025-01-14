package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.meli.footapi.dto.EstadioDto;
import com.meli.footapi.entity.Estadio;
import com.meli.footapi.repository.EstadioRepository;
import com.meli.footapi.validation.EstadioValidation;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    @Autowired
    private EstadioValidation estadioValidation;

    public EstadioDto createStadium(Estadio stadium) {
        estadioValidation.validateStadiumInput(stadium);

        estadioRepository.save(stadium);

        return getStadiumById(stadium.getId());
    }

    public List<EstadioDto> getStadiums() {
        List<EstadioDto> dtoList = new ArrayList<>();
        List<Estadio> stadiumList = estadioRepository.findAll();
    
        for (int i = 0; i < stadiumList.size(); i++) {
            Estadio c = stadiumList.get(i);
            dtoList.add(EstadioDto.estadioToDto(c));
        }

        return dtoList;
    }

    public Page<Estadio> findByNome(String nome, int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Estadio> paginado = estadioRepository.findByNome(nome, paginacao);

        return paginado;
    } 

    public Page<Estadio> findAll(int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Estadio> paginado = estadioRepository.findAll(paginacao);

        return paginado;
    } 


    public EstadioDto getStadiumById(int id) {

        Estadio stadium = this.estadioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum estádio com o ID " + id));

        EstadioDto dto = EstadioDto.estadioToDto(stadium);

        return dto;
    }

    public EstadioDto updateStadium(int id, Estadio updatedStadiumInfo) {
        Estadio estadio = estadioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estadio não encontrado para realizar a alteração. Utilize um id de um estadio já cadastrado para realizar uma alteração."));

        estadio.setId(id);
        estadio.setNome(updatedStadiumInfo.getNome());
        estadio.setClube(updatedStadiumInfo.getClube());

        estadioValidation.validateStadiumInput(estadio);

        estadioRepository.save(estadio);

        EstadioDto dto = EstadioDto.estadioToDto(estadio);

        return dto;
    }

    public void deleteStadium(int id) {
        Estadio stadiumToDelete = estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum estádio com o ID " + id));

        estadioRepository.delete(stadiumToDelete);

    }

    public Map<String, Object> paginarEstadios(String nome, int pagina, int limite) {
        try {
            List<Estadio> estadios = new ArrayList<Estadio>();
            Page<Estadio> paginaEstadio;
            if(nome == null)
                paginaEstadio = findAll(limite, pagina);
            else {
                paginaEstadio = findByNome(nome, limite, pagina);
            }

            estadios = paginaEstadio.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Estadios", estadios);
            response.put("paginaAtual", paginaEstadio.getNumber() + 1);
            response.put("totalDeItens", paginaEstadio.getTotalElements());
            response.put("totalDePaginas", paginaEstadio.getTotalPages());

            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel realizar a busca com os parametros fornecidos");
        }
    }
}

