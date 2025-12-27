package com.devekoc.altaris.dto;

public record ZoneListDTO(
        Integer id,
        String name,
        String description,
        String saintPatron,
        String image,
        String locality,
        ChaplainListDTO chaplain,
        OfficeListDTO office,
        String episcopalVicar,
        String dioceseName
) {
}
