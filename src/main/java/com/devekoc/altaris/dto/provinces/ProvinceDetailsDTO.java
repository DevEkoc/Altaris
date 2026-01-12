package com.devekoc.altaris.dto.provinces;

import com.devekoc.altaris.dto.chaplains.ChaplainListDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Province, with office and chaplain")
public record ProvinceDetailsDTO(
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

        @Schema(description = "Assigned chaplain details")
        ChaplainListDTO chaplain,

        @Schema(description = "Associated administrative office details")
        OfficeListDTO office,

        @Schema(example = "Mvolyé")
        String headquarter,

        @Schema(example = "Jean Mbarga")
        String archbishop
) {}
