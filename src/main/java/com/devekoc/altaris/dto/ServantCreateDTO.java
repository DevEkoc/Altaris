package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ServantCreateDTO(
        @Size(min = 1, max = 50, message = "Le nom du Servant doit contenir entre 1 et 50 caractères.")
        @NotBlank(message = "Le nom du Servant ne doit pas être vide !")
        String name,

        @Size(max = 50, message = "Le prénom du Servant doit contenir au max 50 caractères.")
        String surname,

        @NotNull(message = "La date de naissance ne doit pas être vide !")
        LocalDate birthDate,

        @NotNull(message = "Le sexe ne doit pas être vide !")
        @Enumerated(EnumType.STRING)
        Gender gender,

        LocalDate entryDate,

        @Enumerated(EnumType.STRING)
        ServantGrade grade,

        @ValidCustomPhoneNumber
        @NotBlank(message = "Le téléphone ne doit pas être vide !")
        String phone,

        MultipartFile image,

        @NotNull(message = "L'ID de la Paroisse ne doit pas être vide !")
        Integer parishId
) {
}
