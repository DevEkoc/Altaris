package com.devekoc.altaris.dto.parishes;

import com.devekoc.altaris.enumerations.ParishType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "View of a Parish for listing and details")
public record ParishListDTO(
        @Schema(example = "101") Integer id,
        @Schema(example = "Saint Joseph Parish") String name,
        @Schema(example = "A historic parish located in the city center") String description,
        @Schema(example = "Saint Joseph") String saintPatron,
        @Schema(example = "uploads/parishes/Parish_uuid.jpg") String image,
        @Schema(example = "Anguissa") String locality,
        @Schema(example = "Abbé Marc Ndzana") String priest,
        @Schema(example = "PAROISSE") ParishType parishType,
        @Schema(example = "5") Integer zoneId,
        @Schema(example = "Zone of Mfoundi") String zoneName
) {}
