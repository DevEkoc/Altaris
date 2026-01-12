package com.devekoc.altaris.dto.dioceses;

import com.devekoc.altaris.dto.chaplains.ChaplainListDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.enumerations.DioceseType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed view of a Diocese, with office and chaplain")
public record DioceseDetailsDTO(
        @Schema(example = "1") Integer id,
        @Schema(example = "Diocese of Yokadouma") String name,
        @Schema(example = "Diocese in the East region of Cameroon") String description,
        @Schema(example = "Saint Therese of the Child Jesus") String saintPatron,
        @Schema(example = "uploads/dioceses/Diocese_uuid.jpg") String image,
        @Schema(example = "Yokadouma") String locality,
        @Schema(description = "Assigned chaplain details") ChaplainListDTO chaplain,
        @Schema(description = "Associated administrative office details") OfficeListDTO office,
        @Schema(example = "Mgr Andrew NKEA")String bishop,
        @Schema(example = "Mgr Simon Victor TONYE BAKOT")String retiredBishop,
        @Schema(example = "SUFFRAGANT")DioceseType type,
        @Schema(example = "10")Integer provinceId,
        @Schema(example = "Ecclesiastical Province of Bertoua")String provinceName
) {
}
