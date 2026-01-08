package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.AssignmentCreateDTO;
import com.devekoc.altaris.dto.AssignmentListDTO;
import com.devekoc.altaris.entities.Assignment;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Servant;
import com.devekoc.altaris.mappers.AssignmentMapper;
import com.devekoc.altaris.repositories.AssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final OfficeService officeService;
    private final ServantService servantService;

    protected String entityLabel() {
        return Assignment.class.getSimpleName().toUpperCase();
    }

    public AssignmentListDTO create(AssignmentCreateDTO dto) throws IOException {
        Servant servant = servantService.findByIdOrThrow(dto.servantId());
        Office office = officeService.findByIdOrThrow(dto.officeId());
        // Vérifier que le bureau est bien actif
        if (!office.isActive()) {
            throw new DataIntegrityViolationException("Impossible d'ajouter un servant à un bureau inactif !");
        }
        // que le poste n'existe pas déjà dans le bureau
        if (assignmentRepository.existsByPositionAndOfficeId(dto.position(), office.getId())) {
            throw new DataIntegrityViolationException(String.format("Le poste spécifié (%s) est déjà occupé dans ce bureau !", dto.position()));
        }
        Assignment assignment = AssignmentMapper.fromCreateDTO(dto, new Assignment(), servant, office);
        Assignment saved = assignmentRepository.save(assignment);

        log.info("{} (ID : {}) créé avec succès !", entityLabel(), saved.getId());
        return AssignmentMapper.toListDTO(saved);
    }

    public AssignmentListDTO findById(int id) {
        Assignment found = findByIdOrThrow(id);
        return AssignmentMapper.toListDTO(found);
    }

    public List<AssignmentListDTO> listAll() {
        return assignmentRepository.findAll()
                .stream()
                .map(AssignmentMapper::toListDTO)
                .toList();
    }

    public AssignmentListDTO update(int id, AssignmentCreateDTO dto) throws IOException {
        Assignment found = findByIdOrThrow(id);
        Servant servant = servantService.findByIdOrThrow(dto.servantId());
        Office office = officeService.findByIdOrThrow(dto.officeId());

        if (!office.isActive()) {
            throw new DataIntegrityViolationException("Impossible d'ajouter un servant à un bureau inactif !");
        }
        if (assignmentRepository.existsByPositionAndOfficeId(dto.position(), office.getId())) {
            throw new DataIntegrityViolationException(String.format("Le poste spécifié (%s) est déjà occupé dans ce bureau !", dto.position()));
        }

        Assignment assignment = AssignmentMapper.fromCreateDTO(dto, found, servant, office);
        Assignment updated = assignmentRepository.save(assignment);

        log.info("{} (ID: {}) mis à jour avec succès.", entityLabel(), id);

        return AssignmentMapper.toListDTO(updated);
    }

    public void delete(int id) {
        Assignment found = findByIdOrThrow(id);
        assignmentRepository.delete(found);
        log.info("{} (ID: {}) supprimé avec succès.", entityLabel(), found.getId());
    }

    private Assignment findByIdOrThrow(int id) {
        return assignmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID '%d' !", entityLabel(), id)
                )
        );
    }
}
