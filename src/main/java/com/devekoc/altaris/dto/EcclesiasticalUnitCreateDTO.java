package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@Schema(description = "Base schema for creating any ecclesiastical unit")
public abstract class EcclesiasticalUnitCreateDTO{
    @NotBlank(message = "Le nom de l'unité ne doit pas être vide !")
    @Size(min = 5, max = 50, message = "Le nom doit contenir entre 5 et 50 caractères.")
    @Schema(example = "Province of Yaoundé", description = "Unique name of the unit")
    private String name;

    @NotBlank(message = "La description ne doit pas être vide !")
    @Size(min = 10, max = 2000, message = "La description doit contenir entre 10 et 2000 caractères.")
    @Schema(type = "string", format = "text", minLength = 10, maxLength = 2000, example = "Comprising 7 dioceses, this province extends across the Center and South regions of Cameroon, covering all of their departments except Nyong-Ekélé.", description = "Detailed description of the unit")
    private String description;

    @Size(max = 50, message = "Le nom du Saint Patron doit contenir au max 50 caractères.")
    @Schema(example = "Saint Therese", description = "Patron saint of the unit")
    private String saintPatron;

    @Schema(description = "Cover image file (jpg, jpeg, png, webp)")
    private MultipartFile image;

    @Schema(example = "Yaoundé", description = "Geographical location of the unit")
    private String locality;

    @Schema(example = "1", description = "ID of the assigned chaplain")
    private Integer chaplainId;

    protected EcclesiasticalUnitCreateDTO(
            String name,
            String description,
            String saintPatron,
            String locality,
            MultipartFile image,
            Integer chaplainId
    ) {
        this.name = name;
        this.description = description;
        this.saintPatron = saintPatron;
        this.locality = locality;
        this.image = image;
        this.chaplainId = chaplainId;
    }
}
