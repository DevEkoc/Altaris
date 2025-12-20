package com.devekoc.altaris.dto;

import com.devekoc.altaris.entities.EcclesiasticalUnit;
import com.devekoc.altaris.enumerations.LocalityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record LocalityListDTO(
        String name,
        @Enumerated(EnumType.STRING) LocalityType localityType,
        EcclesiasticalUnit ecclesiasticalUnit
) {
}
