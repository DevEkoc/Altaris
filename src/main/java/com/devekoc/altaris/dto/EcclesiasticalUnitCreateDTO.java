package com.devekoc.altaris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public abstract class EcclesiasticalUnitCreateDTO{
    @NotBlank(message = "Le nom de l'unité ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le nom doit contenir entre 1 et 50 caractères.")
    private String name;

    @NotBlank(message = "La description ne doit pas être vide !")
    private String description;

    @Size(min = 1, max = 50, message = "Le nom du Saint Patron doit contenir entre 1 et 50 caractères.")
    private String saintPatron;

    private MultipartFile image;

    private String locality;

    private Integer chaplainId;

    private Integer officeId;

    protected EcclesiasticalUnitCreateDTO(
            String name,
            String description,
            String saintPatron,
            String locality,
            MultipartFile image,
            Integer chaplainId,
            Integer officeId
    ) {
        this.name = name;
        this.description = description;
        this.saintPatron = saintPatron;
        this.locality = locality;
        this.image = image;
        this.chaplainId = chaplainId;
        this.officeId = officeId;
    }
}
