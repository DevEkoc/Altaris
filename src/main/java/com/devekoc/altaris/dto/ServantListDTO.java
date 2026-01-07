package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record ServantListDTO(
        Integer id,
        String serialNumber,
        String name,
        String surname,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate birthDate,
        @Enumerated(EnumType.STRING)Gender gender,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate entryDate,
        @Enumerated(EnumType.STRING)ServantGrade grade,
        String phone,
        String image,
        String parishName
) {
}
