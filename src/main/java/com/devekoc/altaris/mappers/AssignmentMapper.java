package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.assignments.AssignmentCreateDTO;
import com.devekoc.altaris.dto.assignments.AssignmentListDTO;
import com.devekoc.altaris.entities.Assignment;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Servant;

import java.util.List;

public class AssignmentMapper {
    public static Assignment fromCreateDTO(AssignmentCreateDTO dto, Assignment assignment, Servant servant, Office office) {
        assignment.setPosition(dto.position());

        // Filtrage des doublons et les chaines vides
        List<String> cleanedMissions = dto.missions()
                .stream()
                .filter(mission -> mission != null && !mission.trim().isEmpty())
                .map(String :: trim)
                .distinct()
                .sorted()
                .toList();
        assignment.setMissions(cleanedMissions);

        assignment.setOffice(office);
        assignment.setServant(servant);
        return assignment;
    }
    public static AssignmentListDTO toListDTO (Assignment assignment) {
        return new AssignmentListDTO (
                assignment.getId(),
                assignment.getPosition(),
                assignment.getMissions(),
                assignment.getServant().getId(),
                assignment.getServant().getName()
        );
    }
}