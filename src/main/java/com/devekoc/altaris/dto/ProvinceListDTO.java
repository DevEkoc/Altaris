package com.devekoc.altaris.dto;

public record ProvinceListDTO(
        Integer id,
        String name,
        String description,
        String saintPatron,
        String image,
        String headquarter,
        String archbishop,
        String localite,
        ChaplainListDTO chaplain,
        OfficeListDTO office

) {
}
