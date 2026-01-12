package com.devekoc.altaris.dto.servants;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Schema(description = "Display data for a servant")
public record ServantListDTO(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "SER2026001", description = "System-generated unique identifier")
        String serialNumber,

        @Schema(example = "Jean-Baptiste")
        String name,

        @Schema(example = "Mvogo")
        String surname,

        @Schema(example = "15-08-2010")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate birthDate,

        @Schema(example = "MASCULIN")
        @Enumerated(EnumType.STRING)
        Gender gender,

        @Schema(example = "12-01-2023")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate entryDate,

        @Schema(example = "ACOLYTE")
        @Enumerated(EnumType.STRING)
        ServantGrade grade,

        @Schema(example = "670000000")
        String phone,

        @Schema(example = "uploads/servants/Servant_uuid.jpg")
        String image,

        @Schema(example = "12")
        Integer parishId,

        @Schema(example = "Paroisse de la Trinité")
        String parishName
) {}