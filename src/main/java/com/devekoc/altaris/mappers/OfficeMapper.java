package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.assignments.AssignmentListDTO;
import com.devekoc.altaris.dto.offices.OfficeCreateDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.entities.EcclesiasticalUnit;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.enumerations.EcclesiasticalLevel;

import java.util.List;

public class OfficeMapper {
    public static Office fromCreateDTO(OfficeCreateDTO dto, Office office, EcclesiasticalUnit unit, EcclesiasticalLevel level) {
        office.setCreationDate(dto.creationDate());
        office.setDescription(dto.description());
        office.setEcclesiasticalLevel(level);
        office.setUnit(unit);
        return office;
    }
    public static OfficeListDTO toListDTO (Office office, List<AssignmentListDTO> assignments) {
//        // L'Office null est autorisé lors de la création d'une unité
        // N'est plus utile, car on crée désormais l'unité sans office
//        if (office == null) return null;

        return new OfficeListDTO (
                office.getId(),
                office.getCreationDate(),
                office.isActive(),
                office.getDescription(),
                office.getEcclesiasticalLevel(),
                office.getUnit().getId(),
                office.getUnit().getName(),
                assignments
        );
    }
}