package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.OfficeCreateDTO;
import com.devekoc.altaris.dto.OfficeListDTO;
import com.devekoc.altaris.entities.Office;

public class OfficeMapper {
    public static Office fromCreateDTO(OfficeCreateDTO dto, Office office) {
        office.setCreationDate(dto.creationDate());
        office.setDescription(dto.description());
        office.setDescription(dto.description());
        return office;
    }
    public static OfficeListDTO toListDTO (Office office) {
        // L'Office null est autorisé lors de la création d'une unité
        if (office == null) return null;

        return new OfficeListDTO (
                office.getId(),
                office.getCreationDate(),
                office.isActive(),
                office.getDescription()
        );
    }
}