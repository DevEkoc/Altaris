package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.DioceseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "Data required to create or update a Diocese")
public class DioceseCreateDTO extends EcclesiasticalUnitCreateDTO {
    @Size(min = 1, max = 50, message = "Le nom de l'Évêque doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom de l'Évêque ne doit pas être vide !")
    @Schema(example = "Mgr Andrew NKEA", description = "The name of the diocesan bishop")
    private final String bishop;

    @Size(max = 50, message = "Le nom de l'Évêque émérite doit contenir au max 50 caractères.")
    @Schema(example = "Mgr Simon Victor TONYE BAKOT", description = "The name of the emeritus bishop of the diocese (if any)")
    private final String retiredBishop;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type du Diocèse ne doit pas être vide !")
    @Schema(example = "SUFFRAGANT", description = "The Diocese type: ARCHIDIOCESE or SUFFRAGANT")
    private final DioceseType type;

    @NotNull(message = "L'ID de la Province ne doit pas être null !")
    @Schema(example = "10", description = "ID of the parent province")
    private final Integer provinceId;

    public DioceseCreateDTO (
            String name,
            String description,
            String saintPatron,
            MultipartFile image,
            String locality,
            Integer chaplainId,
            Integer officeId,
            String bishop,
            String retiredBishop,
            DioceseType type,
            Integer provinceId)
    {
        super(name,  description, saintPatron, locality, image, chaplainId, officeId);
        this.bishop = bishop;
        this.retiredBishop = retiredBishop;
        this.type = type;
        this.provinceId = provinceId;
    }


}
