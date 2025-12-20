package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.PriestlyRank;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ChaplainListDTO(
        Integer id,
        String name,
        String surname,
        @Enumerated(EnumType.STRING)PriestlyRank priestlyRank,
        String phone
        ) {
}
