package com.meli.footapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.footapi.dto.ClubeDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.repository.ClubeRepository;
import com.meli.footapi.validation.ClubeValidation;

@Service
public class ClubeService {

    private static final ModelMapper mapper = new ModelMapper();

    @Autowired
    protected ClubeRepository clubeRepository;

    @Autowired
    protected ClubeValidation clubeValidation;

    public ClubeDto createClub(Clube clube) {
        clubeValidation.validateClubInput(clube);
        clubeRepository.save(clube);

        int id = clube.getId();

        return getClubeById(id);
    }

    public Map<String, Object> getPaginatedClubs(@Nullable String nome, int pagina, int limite) {
        try {
            List<Clube> clubes = new ArrayList<Clube>();
            Page<Clube> paginaClube;
            if(nome == null)
                paginaClube = getClubesPaginados(limite, pagina);
            else {
                paginaClube = getClubeByNome(nome, limite, pagina);
            }

            clubes = paginaClube.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("Clubes", clubes);
            response.put("paginaAtual", paginaClube.getNumber() + 1);
            response.put("totalDeItens", paginaClube.getTotalElements());
            response.put("totalDePaginas", paginaClube.getTotalPages());

            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel realizar a busca com os parametros fornecidos");
        }
    }

  
    public Page<Clube> getClubeByNome(String nome, int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Clube> paginado = clubeRepository.findByNomeContaining(nome, paginacao);

        return paginado;
    } 

    public Page<Clube> getClubesPaginados(int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Clube> paginado = clubeRepository.findAll(paginacao);

        return paginado;
    } 

    public ClubeDto getClubeById(int id) {
        Clube clube = this.clubeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum clube com o ID " + id));

        ClubeDto dto = mapper.map(clube, ClubeDto.class);

        return dto;
    }

    public ClubeDto updateClube(int id, Clube novosDadosDoClube) {
        ClubeDto clubeParaAtualizarDto = getClubeById(id);
     
        clubeValidation.validateClubInput(novosDadosDoClube);

        clubeParaAtualizarDto.setNome(novosDadosDoClube.getNome());
        clubeParaAtualizarDto.setEstado(novosDadosDoClube.getEstado());
        clubeParaAtualizarDto.setAtivo(novosDadosDoClube.isAtivo());
        clubeParaAtualizarDto.setDataDeCriacao(novosDadosDoClube.getDataDeCriacao());

        Clube clubeAtualizado = mapper.map(clubeParaAtualizarDto, Clube.class);
        clubeRepository.save(clubeAtualizado);

        return clubeParaAtualizarDto;
    }

    public void deleteClube(int id) {
        Clube clubToDelete = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum clube com o id " + id));

        clubeRepository.delete(clubToDelete);

    }

    public List<ClubeDto> getClubes() {
        List<Clube> listaDeClubes = clubeRepository.findAll();

        List<ClubeDto> listaDeClubesDto = new ArrayList<>();

        listaDeClubes.forEach(clube -> {
            ClubeDto dto = mapper.map(clube, ClubeDto.class);
            listaDeClubesDto.add(dto);
        });
        return listaDeClubesDto;
    }
}
