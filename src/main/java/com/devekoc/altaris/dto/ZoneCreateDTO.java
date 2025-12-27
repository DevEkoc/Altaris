package com.devekoc.altaris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ZoneCreateDTO extends EcclesiasticalUnitCreateDTO {
    @Size(min = 1, max = 50, message = "Le nom du Vicaire Episcopal doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Vicaire Episcopal ne doit pas être vide !")
    private final String episcopalVicar;

    @NotNull(message = "L'ID du Diocèse ne doit pas être null !")
    private final Integer dioceseId;

    public ZoneCreateDTO(
            String name,
            String description,
            String saintPatron,
            MultipartFile image,
            String locality,
            Integer chaplainId,
            Integer officeId,
            String episcopalVicar,
            Integer dioceseId)
    {
        super(name,  description, saintPatron, locality, image, chaplainId, officeId);
        this.episcopalVicar = episcopalVicar;
        this.dioceseId = dioceseId;
    }


}
