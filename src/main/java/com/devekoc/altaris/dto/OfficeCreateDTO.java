package com.devekoc.altaris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record OfficeCreateDTO(
        @NotNull(message = "La date de création ne doit pas être vide !")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate creationDate,

        boolean active,

        @NotBlank(message = "La description ne doit pas être vide !")
        String description
) {
}
