package com.devekoc.altaris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ProvinceCreateDTO extends EcclesiasticalUnitCreateDTO{

    @Size(min = 1, max = 50, message = "Le siège de la Province doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le siège de la Province ne doit pas être vide !")
    private String headquarter;

    @Size(min = 1, max = 50, message = "Le nom de l'Archevêque de la Province doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom de l'Archevêque de la Province ne doit pas être vide !")
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
