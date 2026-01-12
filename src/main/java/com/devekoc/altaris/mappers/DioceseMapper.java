package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.dioceses.DioceseCreateDTO;
import com.devekoc.altaris.dto.dioceses.DioceseDetailsDTO;
import com.devekoc.altaris.dto.dioceses.DioceseListDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Province;

public final class DioceseMapper extends EcclesiasticalUnitMapper {
    public static Diocese fromCreateDTO (DioceseCreateDTO dto, Diocese diocese, Province province, Chaplain chaplain, String imagePath) {
        mapCommonFields(dto, diocese, chaplain, imagePath);
        diocese.setBishop(dto.getBishop());
        diocese.setRetiredBishop(dto.getRetiredBishop());
        diocese.setType(dto.getType());
        diocese.setProvince(province);
        return diocese;
    }

    public static DioceseListDTO toListDTO(Diocese diocese) {
        return new DioceseListDTO(
                diocese.getId(),
                diocese.getName(),
                diocese.getDescription(),
                diocese.getSaintPatron(),
                diocese.getImage(),
                diocese.getLocality(),
                diocese.getBishop(),
                diocese.getRetiredBishop(),
                diocese.getType(),
                diocese.getProvince().getId(),
                diocese.getProvince().getName()
        );
    }

    public static DioceseDetailsDTO toDetailsDTO(Diocese diocese, OfficeListDTO office) {
        return new DioceseDetailsDTO(
                diocese.getId(),
                diocese.getName(),
                diocese.getDescription(),
                diocese.getSaintPatron(),
                diocese.getImage(),
                diocese.getLocality(),
                ChaplainMapper.toListDTO(diocese.getChaplain()),
                office,
                diocese.getBishop(),
                diocese.getRetiredBishop(),
                diocese.getType(),
                diocese.getProvince().getId(),
                diocese.getProvince().getName()
        );
    }
}
