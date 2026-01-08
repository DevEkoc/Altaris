package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "Data required to create or update a Pastoral Zone")
public class ZoneCreateDTO extends EcclesiasticalUnitCreateDTO {
    @Size(min = 5, max = 50, message = "Le nom du Vicaire Episcopal doit contenir entre 5 et 50 caractères.")
    @NotBlank(message = "Le nom du Vicaire Episcopal ne doit pas être vide !")
    @Schema(example = "Abbé Jean-Pierre", description = "The priest appointed as Episcopal Vicar for this zone")
    private final String episcopalVicar;

    @NotNull(message = "L'ID du Diocèse ne doit pas être null !")
    @Schema(example = "5", description = "Unique identifier of the parent diocese")
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
