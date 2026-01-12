package com.devekoc.altaris.dto.zones;

import com.devekoc.altaris.dto.chaplains.ChaplainListDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Zones, with office and chaplain")
public record ZoneDetailsDTO(
        @Schema(example = "1") Integer id,
        @Schema(example = "Zone of Mendong") String name,
        @Schema(example = "Pastoral zone covering the subdivisions of Yaoundé 3 and 6 ") String description,
        @Schema(example = "Saint Tarcisius") String saintPatron,
        @Schema(example = "uploads/zones/Zone_uuid.png") String image,
        @Schema(example = "Mendong") String locality,
        @Schema(description = "Assigned chaplain details") ChaplainListDTO chaplain,
        @Schema(description = "Associated administrative office details") OfficeListDTO office,
        @Schema(example = "Abbé Jean-Pierre") String episcopalVicar,
        @Schema(example = "20") Integer dioceseId,
        @Schema(example = "Archdiocese of Yaoundé") String dioceseName
) {}
