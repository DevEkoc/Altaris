package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record ServantListDTO(
        Integer id,
        String serialNumber,
        String name,
        String surname,
        LocalDate birthDate,
        @Enumerated(EnumType.STRING)Gender gender,
        LocalDate entryDate,
        @Enumerated(EnumType.STRING)ServantGrade grade,
        String phone,
        String image,
        String parishName
) {
}
