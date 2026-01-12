package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.chaplains.ChaplainCreateDTO;
import com.devekoc.altaris.dto.chaplains.ChaplainListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.mappers.ChaplainMapper;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.specifications.ChaplainSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChaplainService {
    private final ChaplainRepository chaplainRepository;

    protected String entityLabel() {
        return Chaplain.class.getSimpleName().toUpperCase();
    }

    public ChaplainListDTO create(ChaplainCreateDTO dto) {
        Chaplain chaplain = ChaplainMapper.fromCreateDTO(dto, new Chaplain());
        Chaplain saved = chaplainRepository.save(chaplain);

        log.info("{} : {} (ID : {}) créé avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ChaplainMapper.toListDTO(saved);
    }

    public ChaplainListDTO findById(int id) {
        Chaplain found = findByIdOrThrow(id);
        return ChaplainMapper.toListDTO(found);
    }

    public List<ChaplainListDTO> find(String query) {
        Specification<@NonNull Chaplain> spec = ChaplainSpecification.globalSearch(query);
        return chaplainRepository.findAll(spec)
                .stream()
                .map(ChaplainMapper::toListDTO)
                .toList();
    }

    public List<ChaplainListDTO> listAll() {
        return chaplainRepository.findAll()
                .stream()
                .map(ChaplainMapper::toListDTO)
                .toList();
    }

    public ChaplainListDTO update(int id, ChaplainCreateDTO dto) {
        Chaplain chaplain = ChaplainMapper.fromCreateDTO(dto, findByIdOrThrow(id));
        Chaplain saved = chaplainRepository.save(chaplain);

        log.info("{} '{}' (ID: {}) mis à jour avec succès.", entityLabel(), saved.getName(), id);

        return ChaplainMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Chaplain found = findByIdOrThrow(id);
        if (!found.getUnitList().isEmpty()) {
            throw new DataIntegrityViolationException("Impossible de supprimer un Aumônier gérant des unités !");
        }
        chaplainRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimé avec succès.", entityLabel(), found.getName(), found.getId());
    }

    private Chaplain findByIdOrThrow(int id) {
        return chaplainRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID '%d' !", entityLabel(), id)
                )
        );
    }
}
