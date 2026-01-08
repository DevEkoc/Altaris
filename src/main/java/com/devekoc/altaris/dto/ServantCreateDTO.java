package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "Registration form for an Altar Server")
public record ServantCreateDTO(
        @Size(min = 1, max = 50, message = "Le nom du Servant doit contenir entre 1 et 50 caractères.")
        @NotBlank(message = "Le nom du Servant ne doit pas être vide !")
        @Schema(example = "Mvogo")
        String name,

        @Size(max = 50, message = "Le prénom du Servant doit contenir au max 50 caractères.")
        @Schema(example = "Jean Baptiste")
        String surname,

        @NotNull(message = "La date de naissance ne doit pas être vide !")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(example = "15-08-2010", pattern = "dd-MM-yyyy", description = "Format: DD-MM-YYYY")
        LocalDate birthDate,

        @NotNull(message = "Le sexe ne doit pas être vide !")
        @Enumerated(EnumType.STRING)
        @Schema(example = "MASCULIN")
        Gender gender,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(example = "15-08-2010", pattern = "dd-MM-yyyy", description = "Format: DD-MM-YYYY")
        LocalDate entryDate,

        @Enumerated(EnumType.STRING)
        @Schema(example = "ACOLYTE")
        ServantGrade grade,

        @ValidCustomPhoneNumber
        @NotBlank(message = "Le téléphone ne doit pas être vide !")
        @Schema(example = "670123456", description = "Must follow Cameroon phone format (6XXXXXXXX)")
        String phone,

        @Schema(description = "Profile picture file")
        MultipartFile image,

        @NotNull(message = "L'ID de la Paroisse ne doit pas être vide !")
        @Schema(example = "42", description = "ID of the home parish")
        Integer parishId
) {
}
