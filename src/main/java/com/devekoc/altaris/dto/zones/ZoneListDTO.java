package com.devekoc.altaris.dto.zones;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Zone for listing purposes")
public record ZoneListDTO(
        @Schema(example = "1") Integer id,
        @Schema(example = "Zone of Mendong") String name,
        @Schema(example = "Pastoral zone covering the subdivisions of Yaoundé 3 and 6 ") String description,
        @Schema(example = "Saint Tarcisius") String saintPatron,
        @Schema(example = "uploads/zones/Zone_uuid.png") String image,
        @Schema(example = "Mendong") String locality,
        @Schema(example = "Abbé Jean-Pierre") String episcopalVicar,
        @Schema(example = "20") Integer dioceseId,
        @Schema(example = "Archdiocese of Yaoundé") String dioceseName
) {}
