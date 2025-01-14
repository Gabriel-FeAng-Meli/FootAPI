package com.meli.footapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.meli.footapi.dto.RankingDto;
import com.meli.footapi.dto.RetrospectivaDto;
import com.meli.footapi.entity.Clube;
import com.meli.footapi.entity.Partida;
import com.meli.footapi.enums.ValidBrazilStates;
import com.meli.footapi.repository.ClubeRepository;

@Service
public class ClubeService {

    @Autowired
    protected ClubeRepository clubRepo;

    @Autowired
    protected PartidaService partidaService;

    public ClubeDto createClub(Clube clube) {
        validateClubInput(clube);
        clubRepo.save(clube);

        int id = clube.getId();

        return getClubById(id);
    }

    public Map<String, Object> getPaginatedClubs(@Nullable String nome, int page, int size) {
        try {
            List<Clube> clubes = new ArrayList<Clube>();
            Page<Clube> paginaClube;
            if(nome == null)
                paginaClube = findAll(size, page);
            else {
                paginaClube = findByNomeDoClube(nome, size, page);
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

  
    public Page<Clube> findByNomeDoClube(String nome, int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Clube> paginado = clubRepo.findByNomeContaining(nome, paginacao);

        return paginado;
    } 

    public Page<Clube> findAll(int size, int page) {
        Pageable paginacao = PageRequest.of(page, size);
        Page<Clube> paginado = clubRepo.findAll(paginacao);

        return paginado;
    } 

    public ClubeDto getClubById(int id) {

        ModelMapper mapper = new ModelMapper();

        Clube club = this.clubRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Não foi encontrado nenhum clube com o ID " + id));

        ClubeDto dto = mapper.map(club, ClubeDto.class);

        return dto;
    }

    public ClubeDto updateClub(int id, Clube updatedClubInfo) {
        ClubeDto clubToBeUpdated = getClubById(id);

        ModelMapper mapper = new ModelMapper();
        
        validateClubInput(updatedClubInfo);

        clubToBeUpdated.setNome(updatedClubInfo.getNome());
        clubToBeUpdated.setEstado(updatedClubInfo.getEstado());
        clubToBeUpdated.setAtivo(updatedClubInfo.isAtivo());
        clubToBeUpdated.setDataDeCriacao(updatedClubInfo.getDataDeCriacao());

        Clube updatedClub = mapper.map(clubToBeUpdated, Clube.class);
        clubRepo.save(updatedClub);

        return clubToBeUpdated;
    }

    public void deleteClub(int id) {
        Clube clubToDelete = clubRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum clube com o id " + id));

        clubRepo.delete(clubToDelete);

    }

    public RetrospectivaDto getRetrospectiva(int clubId) {
        Clube clube = ClubeDto.dtoToClube(getClubById(clubId));
        String titulo = clube.getNome() + "-" + clube.getEstado();

        List<Partida> listaDePartidas = partidaService.getPartidasByClube(clube);
        
        RetrospectivaDto retro = new RetrospectivaDto(clube, listaDePartidas);
        retro.setTitulo(titulo);

        return retro;
    }

    public RetrospectivaDto getConfrontosDiretos(int clubId, int otherClubId) {
        Clube clubeUm = ClubeDto.dtoToClube(getClubById(clubId));
        Clube clubeDois = ClubeDto.dtoToClube(getClubById(otherClubId));
        
        String titulo = clubeUm.getNome() + "-" + clubeUm.getEstado() + " X " + clubeDois.getNome() + "-" + clubeDois.getEstado();
        
        RetrospectivaDto retro = getRetrospectiva(clubId);
        retro.setTitulo(titulo);

        return retro;
    }
    
    public List<RetrospectivaDto> getRetrospectivaParaCadaAdversario(int clubId) {
        Clube clube = ClubeDto.dtoToClube(getClubById(clubId));
        
        Set<Integer> idTimesEnfrentados = RetrospectivaDto.getTimesEnfrentadosPorClube(clube, partidaService.getPartidasByClube(clube));
        
        List<RetrospectivaDto> retrospectivas = new ArrayList<>();
        
        idTimesEnfrentados.stream().forEach(id -> {
            retrospectivas.add(getConfrontosDiretos(clubId, id));
        });

        return retrospectivas;
    }

    public List<RankingDto> getRanking() {
        List<RankingDto> unsortedRanking= new ArrayList<>();
        
        List<ClubeDto> todosOsClubes = getClubs();

        todosOsClubes.forEach(clube -> {
            int id = clube.getId();
            RetrospectivaDto retro = getRetrospectiva(id);
            RankingDto rank = new RankingDto();
            rank.setClube(clube);
            rank.setPontuacao(retro.getPontuação());

            unsortedRanking.add(rank);
        });

        List<RankingDto> ranking = unsortedRanking.stream().sorted((r1, r2) -> Integer.compare(r2.getPontuacao(), r1.getPontuacao())).filter(rank -> rank.getPontuacao() != 0).toList();

        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setRank(i + 1);
        }

        return ranking;
    }

    public List<ClubeDto> getClubs() {
        ModelMapper mapper = new ModelMapper();
        List<Clube> clubList = clubRepo.findAll();

        List<ClubeDto> dtoList = new ArrayList<>();

        clubList.forEach(clube -> {
            ClubeDto dto = mapper.map(clube, ClubeDto.class);
            dtoList.add(dto);
        });
        return dtoList;
    }


    private void validateClubInput(Clube clubToValidate) {

        String nomeDoClube = clubToValidate.getNome();
        String estadoDoClube = clubToValidate.getEstado();
        validateClubName(nomeDoClube);
        validateClubState(estadoDoClube);
        validateClubDate(clubToValidate.getDataDeCriacao());
        checkIfClubAlreadyExists(getClubs(), nomeDoClube, estadoDoClube);
    }


    private void validateClubDate(LocalDate inputedDate) {
        if (inputedDate != null && inputedDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de criação do clube não pode ser no futuro. Insira uma data valida no formato YYYY-MM-DD");
        }
    }

    private void validateClubState(String inputedState) {
        boolean validState = false;
        for (ValidBrazilStates state : ValidBrazilStates.values()) {
            if (inputedState != null && state.toString().equals(inputedState.toUpperCase())) {
                validState = true;
            }
        }
        if (!validState) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estado escolhido não é um estado real do Brasil. Favor utilizar a sigla de um estado existente (exemplo: SP)");
        };
    }

    private void validateClubName(String inputedName) {
        if (inputedName == null || inputedName.isBlank() || inputedName.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do clube deve conter no mínimo 2 letras.");
        }
    }

    private void checkIfClubAlreadyExists(List<ClubeDto> existingClubs, String inputedName, String inputedState) {
        existingClubs.stream().forEach(existingClub -> {
            if(existingClub.getNome().equals(inputedName) && existingClub.getEstado().equals(inputedState)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um clube de mesmo nome e mesmo estado já está cadastrado.");
            }
        });
    }
}
