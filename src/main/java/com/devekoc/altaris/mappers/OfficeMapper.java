package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.OfficeCreateDTO;
import com.devekoc.altaris.dto.OfficeListDTO;
import com.devekoc.altaris.entities.Office;

public class OfficeMapper {
    public static Office fronCreateDTO(OfficeCreateDTO dto, Office office) {
        office.setCreationDate(dto.creationDate());
        office.setDescription(dto.description());
        office.setDescription(dto.description());
        return office;
    }
    public static OfficeListDTO toListDTO (Office office) {
        if (office == null) return null;
        return new OfficeListDTO (
                office.getId(),
                office.getCreationDate(),
                office.isActive(),
                office.getDescription()
        );
    }
}