package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.DioceseType;
import io.swagger.v3.oas.annotations.media.Schema;

public record DioceseListDTO(
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
        @Schema(example = "Ecclesiastical Province of Bertoua")String provinceName
) {
}
