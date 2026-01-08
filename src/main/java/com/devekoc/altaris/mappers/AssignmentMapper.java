package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.AssignmentCreateDTO;
import com.devekoc.altaris.dto.AssignmentListDTO;
import com.devekoc.altaris.entities.Assignment;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Servant;

public class AssignmentMapper {
    public static Assignment fromCreateDTO(AssignmentCreateDTO dto, Assignment assignment, Servant servant, Office office) {
        assignment.setPosition(dto.position());
        assignment.setMissions(dto.missions());
        assignment.setOffice(office);
        assignment.setServant(servant);
        return assignment;
    }
    public static AssignmentListDTO toListDTO (Assignment assignment) {
        return new AssignmentListDTO (
                assignment.getId(),
                assignment.getPosition(),
                assignment.getMissions(),
                ServantMapper.toListDTO(assignment.getServant()),
                OfficeMapper.toListDTO(assignment.getOffice())
        );
    }
}