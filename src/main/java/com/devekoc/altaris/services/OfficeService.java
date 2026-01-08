package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.OfficeCreateDTO;
import com.devekoc.altaris.dto.OfficeListDTO;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.mappers.OfficeMapper;
import com.devekoc.altaris.repositories.AssignmentRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.specifications.OfficeSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final AssignmentRepository assignmentRepository;

    protected String entityLabel() {
        return Office.class.getSimpleName().toUpperCase();
    }

    public OfficeListDTO create(OfficeCreateDTO dto) throws IOException {
        Office office = OfficeMapper.fromCreateDTO(dto, new Office());
        Office saved = officeRepository.save(office);

        log.info("{} (ID : {}) créé avec succès !", entityLabel(), saved.getId());
        return OfficeMapper.toListDTO(saved);
    }

    public OfficeListDTO findById(int id) {
        Office found = findByIdOrThrow(id);
        return OfficeMapper.toListDTO(found);
    }

    public List<OfficeListDTO> find(String query) {
        Specification<@NonNull Office> spec = OfficeSpecification.globalSearch(query);
        return officeRepository.findAll(spec)
                .stream()
                .map(OfficeMapper::toListDTO)
                .toList();
    }

    public List<OfficeListDTO> listAll() {
        return officeRepository.findAll()
                .stream()
                .map(OfficeMapper::toListDTO)
                .toList();
    }

    public OfficeListDTO update(int id, OfficeCreateDTO dto) throws IOException {
        Office office = OfficeMapper.fromCreateDTO(dto, findByIdOrThrow(id));
        Office saved = officeRepository.save(office);

        log.info("{} (ID: {}) mis à jour avec succès.", entityLabel(), id);

        return OfficeMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Office found = findByIdOrThrow(id);
        if (assignmentRepository.existsByOfficeId(found.getId())) {
            throw new DataIntegrityViolationException("Impossible de supprimer un bureau contenant des servants !");
        }
        officeRepository.delete(found);
        log.info("{} (ID: {}) supprimé avec succès.", entityLabel(), found.getId());
    }

    public Office findByIdOrThrow(int id) {
        return officeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID '%d' !", entityLabel(), id)
                )
        );
    }
}
