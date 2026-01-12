package com.devekoc.altaris.dto.chaplains;

import com.devekoc.altaris.enumerations.PriestlyRank;
import com.devekoc.altaris.validation.NoEcclesiasticalTitle;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Data required to create or update a Chaplain")
public record ChaplainCreateDTO(
        @NotBlank(message = "Le nom de l'Aumônier ne doit pas être vide !")
        @Size(min = 1, max = 50, message = "Le nom de l'Aumônier doit contenir entre 1 et 50 caractères.")
        @NoEcclesiasticalTitle
        @Schema(example = "Mbarga Atangana", description = "The name of the Chaplain")
        String name,

        @Size(max = 50, message = "Le prénom nom de l'Aumônier doit contenir au max 50 caractères.")
        @NoEcclesiasticalTitle
        @Schema(example = "José Martin", description = "The surname of the Chaplain")
        String surname,

        @NotNull(message = "Le niveau sacerdotal de l'Aumônier ne doit pas être vide !")
        @Schema(example = "DIACRE", description = "The sacerdotal level of the Chaplain.")
        PriestlyRank priestlyRank,

        @ValidCustomPhoneNumber
        @NotBlank(message = "Le téléphone ne doit pas être vide !")
        @Schema(example = "690123456", description = "The phone number of the Chaplain")
        String phone
) {
}
