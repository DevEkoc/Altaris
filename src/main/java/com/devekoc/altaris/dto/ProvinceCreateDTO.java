package com.devekoc.altaris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProvinceCreateDTO(
        @NotBlank(message = "Le nom de l'unité ne doit pas être vide !")
        @Size(min = 1, max = 50, message = "Le nom doit contenir entre 1 et 50 caractères.")
        String name,

        @NotBlank(message = "La description ne doit pas être vide !")
        String description,
        
        String saintPatron,
        
        @Size(min = 1, max = 50, message = "Le siège de la Province doit contenir entre 1 et 50 caractères.")
        @NotBlank(message = "Le siège de la Province ne doit pas être vide !")
        String headquarter,

        @Size(min = 1, max = 50, message = "Le nom de l'Archevêque de la Province doit contenir entre 1 et 50 caractères.")
        @NotBlank(message = "Le nom de l'Archevêque de la Province ne doit pas être vide !")
        String archbishop,

        MultipartFile image,

        String locality,

        Integer chaplainId,

        Integer officeId
) {
}
