package com.devekoc.altaris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record OfficeCreateDTO(
        @NotNull(message = "La date de création ne doit pas être vide !")
        LocalDate creationDate,

        boolean active,

        @NotBlank(message = "La description ne doit pas être vide !")
        String description
) {
}
