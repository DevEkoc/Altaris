package com.devekoc.altaris.dto.offices;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Data required to create or update an Office")
public record OfficeCreateDTO(
        @NotNull(message = "La date de création ne doit pas être vide !")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(example = "15-08-2010", pattern = "dd-MM-yyyy", description = "Format: DD-MM-YYYY")
        LocalDate creationDate,

        @Schema(example = "true", description = "Is the office active or not ?")
        boolean active,

        @NotBlank(message = "La description ne doit pas être vide !")
        @Schema(example = "Mendong pastoral zone altar servers office", description = "Full description of the office.")
        String description,

//        @NotNull(message = "Le niveau ecclésiastique ne doit pas être vide !")
//        @Schema(example = "ZONE", description = "Ecclesiastical level of the office unit.")
//        EcclesiasticalLevel ecclesiasticalLevel,

        @NotNull(message = "L'ID de l'unité ecclésiastique ne doit pas être vide !")
        @Schema(example = "10", description = "ID of the linked ecclesiastical unit.")
        Integer unitId
) {
}
