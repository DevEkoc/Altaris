package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.*;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;

public final class ProvinceMapper extends EcclesiasticalUnitMapper {
    public static Province fromCreateDTO (ProvinceCreateDTO dto, Province province, Chaplain chaplain, Office office, String imagePath) {
        mapCommonFields(dto, province, chaplain, office, imagePath);
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
                province.getHeadquarter(),
                province.getArchbishop(),
                province.getLocality(),
                ChaplainMapper.toListDTO(province.getChaplain()),
                OfficeMapper.toListDTO(province.getOffice())
        );
    }
}
