package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.assignments.AssignmentListDTO;
import com.devekoc.altaris.dto.offices.OfficeCreateDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.entities.*;
import com.devekoc.altaris.enumerations.EcclesiasticalLevel;
import com.devekoc.altaris.mappers.AssignmentMapper;
import com.devekoc.altaris.mappers.OfficeMapper;
import com.devekoc.altaris.repositories.AssignmentRepository;
import com.devekoc.altaris.repositories.EcclesiasticalUnitBaseRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.specifications.OfficeSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final AssignmentRepository assignmentRepository;
    private final EcclesiasticalUnitBaseRepository ecclesiasticalUnitBaseRepository;

    protected String entityLabel() {
        return Office.class.getSimpleName().toUpperCase();
    }

    public OfficeListDTO create(OfficeCreateDTO dto) {
        EcclesiasticalUnit unit = getEcclesiasticalUnitOrThrow(dto.unitId());
        // On se rassure qu'il n'y a aucun bureau actif pour cette unité
        if (officeRepository.existsByUnitIdAndActiveTrue(unit.getId())) {
            throw new DataIntegrityViolationException(String.format("Un bureau actif existe déjà pour l'unité '%s' (ID : %d)", unit.getName(), unit.getId()));
        }
        Office office = OfficeMapper.fromCreateDTO(dto, new Office(), unit, resolveLevel(unit));
        Office saved = officeRepository.save(office);

        log.info("{} (ID : {}) créé avec succès !", entityLabel(), saved.getId());
        return OfficeMapper.toListDTO(saved, Collections.emptyList());
    }

    public OfficeListDTO findById(int id) {
        Office found = findByIdOrThrow(id);
        List<Assignment> assignments = assignmentRepository.findByOfficeId(found.getId());

        return OfficeMapper.toListDTO(found, getAssignmentLists(assignments));
    }

    public OfficeListDTO findByUnitId(Integer unitId) {
        Office office = officeRepository.findByUnitId(unitId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID d'entité '%d' !", entityLabel(), unitId)
                ));
        return findById(office.getId());
    }


    public List<OfficeListDTO> listAll() {
        List<Office> offices = officeRepository.findAll();
        return getListFromManyLists(offices);
    }

    public List<OfficeListDTO> find(String query) {
        Specification<@NonNull Office> spec = OfficeSpecification.globalSearch(query);
        return getListFromManyLists(officeRepository.findAll(spec));
    }

    public OfficeListDTO update(int id, OfficeCreateDTO dto) {
        Office found = findByIdOrThrow(id);
        EcclesiasticalUnit unit = getEcclesiasticalUnitOrThrow(dto.unitId());
        // On se rassure qu'il n'y a aucun bureau actif pour cette unité
        if (officeRepository.existsByUnitIdAndActiveTrueAndIdNot(unit.getId(), id)) {
            throw new DataIntegrityViolationException(String.format("Un bureau actif existe déjà pour l'unité '%s' (ID : %d)", unit.getName(), unit.getId()));
        }
        EcclesiasticalLevel level = resolveLevel(unit);

        Office office = OfficeMapper.fromCreateDTO(dto, found, unit, level);
        Office saved = officeRepository.save(office);

        log.info("{} (ID: {}) mis à jour avec succès.", entityLabel(), id);

        return OfficeMapper.toListDTO(saved, getAssignmentLists(assignmentRepository.findByOfficeId(office.getId())));
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

    public boolean existsByUnitId(int unitId) {
        return officeRepository.existsByUnitId(unitId);
    }

    public boolean existsByActiveUnitId(int unitId) {
        return officeRepository.existsByUnitIdAndActiveTrue(unitId);
    }

    private List<AssignmentListDTO> getAssignmentLists(List<Assignment> assignments) {
        if (assignments.isEmpty()) return Collections.emptyList();

        return assignments
                .stream()
                .map(AssignmentMapper::toListDTO)
                .toList();
    }

    private List<OfficeListDTO> getListFromManyLists(List<Office> offices) {
        if (offices.isEmpty()) return List.of();

        List<Integer> ids = offices.stream().map(Office::getId).toList();
        List<Assignment> assignments = assignmentRepository.findByOfficeIdIn(ids);

        Map<Integer, List<Assignment>> assignmentsByOffice = assignments.stream()
                .collect(Collectors.groupingBy(assignment -> assignment.getOffice().getId()));

        return offices.stream()
                .map(office -> {
                    List<AssignmentListDTO> assignmentList = assignmentsByOffice
                            .getOrDefault(office.getId(), Collections.emptyList())
                            .stream()
                            .map(AssignmentMapper::toListDTO)
                            .toList();
                    return OfficeMapper.toListDTO(office, assignmentList);
                })
                .toList();
    }

    private EcclesiasticalUnit getEcclesiasticalUnitOrThrow(Integer unitId) {
        return ecclesiasticalUnitBaseRepository.findById(unitId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Unité ecclésiastique introuvable avec l'ID : %s", unitId))
        );
    }

    private EcclesiasticalLevel resolveLevel(EcclesiasticalUnit unit) {
        if (unit instanceof Province) return EcclesiasticalLevel.PROVINCE;
        if (unit instanceof Diocese)  return EcclesiasticalLevel.DIOCESE;
        if (unit instanceof Zone)     return EcclesiasticalLevel.ZONE;
        if (unit instanceof Parish)   return EcclesiasticalLevel.PARISH;

        throw new IllegalArgumentException("Type d'unité ecclésiastique inconnu : " + unit.getClass().getSimpleName());
    }
}
