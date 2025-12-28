package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.ParishType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ParishCreateDTO extends EcclesiasticalUnitCreateDTO {
    @Size(min = 1, max = 50, message = "Le nom du Curé doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Curé ne doit pas être vide !")
    private final String priest;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de la Paroisse ne doit pas être vide !")
    private final ParishType type;

    @NotNull(message = "L'ID de la Zone ne doit pas être null !")
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
