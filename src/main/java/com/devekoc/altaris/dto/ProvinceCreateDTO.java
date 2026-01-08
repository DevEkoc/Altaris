package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@Schema(description = "Data required to create or update an Ecclesiastical Province")
public class ProvinceCreateDTO extends EcclesiasticalUnitCreateDTO{

    @Size(min = 5, max = 50, message = "Le siège de la Province doit contenir entre 5 et 50 caractères.")
    @NotBlank(message = "Le siège de la Province ne doit pas être vide !")
    @Schema(example = "Bamenda", description = "The official seat/headquarters of the province")
    private String headquarter;

    @Size(min = 5, max = 50, message = "Le nom de l'Archevêque de la Province doit contenir entre 5 et 50 caractères.")
    @NotBlank(message = "Le nom de l'Archevêque de la Province ne doit pas être vide !")
    @Schema(example = "Mgr Jean Mbarga", description = "Name of the Metropolitan Archbishop of the province")
    private String archbishop;

    public ProvinceCreateDTO (
            String name,
            String description,
            String saintPatron,
            MultipartFile image,
            String locality,
            Integer chaplainId,
            Integer officeId,
            String Headquarter,
            String Archbishop)
    {
        super(name,  description, saintPatron, locality, image, chaplainId, officeId);
        this.headquarter = Headquarter;
        this.archbishop = Archbishop;
    }


}
