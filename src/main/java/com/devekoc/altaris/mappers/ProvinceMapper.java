package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.dto.provinces.ProvinceCreateDTO;
import com.devekoc.altaris.dto.provinces.ProvinceDetailsDTO;
import com.devekoc.altaris.dto.provinces.ProvinceListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Province;

public final class ProvinceMapper extends EcclesiasticalUnitMapper {
    public static Province fromCreateDTO (ProvinceCreateDTO dto, Province province, Chaplain chaplain, String imagePath) {
        mapCommonFields(dto, province, chaplain, imagePath);
        province.setHeadquarter(dto.getHeadquarter());
        province.setArchbishop(dto.getArchbishop());
        return province;
    }

    public static ProvinceListDTO toListDTO(Province province) {
        return new ProvinceListDTO(
                province.getId(),
                province.getName(),
                province.getDescription(),
                province.getSaintPatron(),
                province.getImage(),
                province.getLocality(),
                province.getHeadquarter(),
                province.getArchbishop()
        );
    }

    public static ProvinceDetailsDTO toDetailsDTO(Province province, OfficeListDTO office) {
        return new ProvinceDetailsDTO(
                province.getId(),
                province.getName(),
                province.getDescription(),
                province.getSaintPatron(),
                province.getImage(),
                province.getLocality(),
                ChaplainMapper.toListDTO(province.getChaplain()),
                office,
                province.getHeadquarter(),
                province.getArchbishop()
        );
    }
}
