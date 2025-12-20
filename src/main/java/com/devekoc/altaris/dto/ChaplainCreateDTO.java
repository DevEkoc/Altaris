package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.PriestlyRank;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChaplainCreateDTO(
        @NotBlank(message = "Le nom de l'Aumônier ne doit pas être vide !")
        @Size(min = 1, max = 50, message = "Le nom de l'Aumônier doit contenir entre 1 et 50 caractères.")
        String name,

        @Size(max = 50, message = "Le prénom nom de l'Aumônier doit contenir au max 50 caractères.")
        String surname,

        @NotNull(message = "Le niveau sacerdotal de l'Aumônier ne doit pas être vide !")
        PriestlyRank priestlyRank,

        @ValidCustomPhoneNumber
        @NotBlank(message = "Le téléphone ne doit pas être vide !")
        String phone
) {
}
