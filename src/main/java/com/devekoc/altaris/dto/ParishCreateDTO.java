package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.ParishType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "Data required to create or update a Parish")
public class ParishCreateDTO extends EcclesiasticalUnitCreateDTO {
    @Size(min = 5, max = 50, message = "Le nom du Curé doit contenir entre 5 et 50 caractères.")
    @NotBlank(message = "Le nom du Curé ne doit pas être vide !")
    @Schema(example = "Abbé Marc Ndzana", description = "Current Parish Priest (Curé)")
    private final String priest;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de la Paroisse ne doit pas être vide !")
    @Schema(example = "PAROISSE", description = "Canonical status of the parish")
    private final ParishType type;

    @NotNull(message = "L'ID de la Zone ne doit pas être null !")
    @Schema(example = "12", description = "ID of the parent Episcopal Zone")
    private final Integer zoneId;

    public ParishCreateDTO(
            String name,
            String description,
            String saintPatron,
            MultipartFile image,
            String locality,
            Integer chaplainId,
            Integer officeId,
            String priest,
            ParishType type,
            Integer zoneId)
    {
        super(name,  description, saintPatron, locality, image, chaplainId, officeId);
        this.priest = priest;
        this.type = type;
        this.zoneId = zoneId;
    }


}
