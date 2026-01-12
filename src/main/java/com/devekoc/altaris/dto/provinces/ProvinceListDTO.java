package com.devekoc.altaris.dto.provinces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Province for list and display purposes")
public record ProvinceListDTO(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "Ecclesiastical Province of Yaoundé")
        String name,

        @Schema(example = "Primary ecclesiastical province in the Center Region")
        String description,

        @Schema(example = "Saint Therese of the Child Jesus")
        String saintPatron,

        @Schema(example = "uploads/provinces/Province_uuid.jpg")
        String image,

        @Schema(example = "Yaoundé")
        String locality,

        @Schema(example = "Mvolyé")
        String headquarter,

        @Schema(example = "Jean Mbarga")
        String archbishop
) {}
