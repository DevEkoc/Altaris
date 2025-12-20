package com.devekoc.altaris.dto;

import java.time.LocalDate;

public record OfficeListDTO(
        Integer id,
        LocalDate creationDate,
        boolean active,
        String description
) {
}
