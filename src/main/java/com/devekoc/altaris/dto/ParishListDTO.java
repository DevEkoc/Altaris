package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.ParishType;

public record ParishListDTO(
        Integer id,
        String name,
        String description,
        String saintPatron,
        String image,
        String locality,
        ChaplainListDTO chaplain,
        OfficeListDTO office,
        String priest,
        ParishType parishType,
        String zoneName
) {
}
