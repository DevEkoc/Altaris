package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Province for list and display purposes")
public record ProvinceListDTO(
        @Schema(example = "1") Integer id,
        @Schema(example = "Province of Yaoundé") String name,
        @Schema(example = "Primary ecclesiastical province in the Center Region") String description,
        @Schema(example = "Saint Therese of the Child Jesus") String saintPatron,
        @Schema(example = "uploads/provinces/Province_uuid.jpg") String image,
        @Schema(example = "Mvolyé") String headquarter,
        @Schema(example = "Jean Mbarga") String archbishop,
        @Schema(example = "Yaoundé") String localite,
        @Schema(description = "Assigned chaplain details") ChaplainListDTO chaplain,
        @Schema(description = "Associated administrative office details") OfficeListDTO office
) {}
