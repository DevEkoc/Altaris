package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Request body for assigning an Altar Server to a specific administrative office position")
public record AssignmentCreateDTO(
        @NotBlank(message = "Le poste ne doit pas être vide !")
        @Size(min = 5, max = 50, message = "Le poste doit contenir entre 5 et 50 caractères.")
        @Schema(example = "Président de Bureau", description = "Official title of the position within the office")
        String position,

        @NotBlank(message = "La description ne doit pas être vide !")
        @Schema(
                example = "[\"Organiser les réunions hebdomadaires\", \"Superviser la formation des aspirants\"]",
                description = "List of specific duties and responsibilities for this assignment"
        )
        List<String> missions,

        @NotNull(message = "L'ID du Servant ne doit pas être vide !")
        @Schema(example = "15", description = "Technical ID of the Altar Server to be assigned")
        Integer servantId,

        @NotNull(message = "L'ID du Bureau ne doit pas être vide !")
        @Schema(example = "3", description = "Technical ID of the Office where the server will serve")
        Integer officeId
) {
}
