package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.DioceseType;

public record DioceseListDTO(
        Integer id,
        String name,
        String description,
        String saintPatron,
        String image,
        String locality,
        ChaplainListDTO chaplain,
        OfficeListDTO office,
        String bishop,
        String retiredBishop,
        DioceseType type,
        String provinceName
) {
}
