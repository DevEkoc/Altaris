package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.*;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;

import java.util.List;

public final class ProvinceMapper {
    public static Province fromCreateDTO (ProvinceCreateDTO dto, Province province, Chaplain chaplain, Office office, String imagePath) {
        province.setName(dto.name());
        province.setDescription(dto.description());
        province.setSaintPatron(dto.saintPatron());
        province.setImage(imagePath);
        province.setLocalite(dto.locality());
        province.setChaplain(chaplain);
        province.setOffice(office);
        province.setHeadquarter(dto.headquarter());
        province.setArchbishop(dto.archbishop());
        return province;
    }

    public static ProvinceListDTO toProvinceListDTO (Province province, ChaplainListDTO chaplain, OfficeListDTO office) {
        return new ProvinceListDTO(
                province.getId(),
                province.getName(),
                province.getDescription(),
                province.getSaintPatron(),
                province.getImage(),
                province.getHeadquarter(),
                province.getArchbishop(),
                province.getLocalite(),
                chaplain,
                office
        );
    }
}
