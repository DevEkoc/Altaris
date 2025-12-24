package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.DioceseCreateDTO;
import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Province;

public final class DioceseMapper extends EcclesiasticalUnitMapper {
    public static Diocese fromCreateDTO (DioceseCreateDTO dto, Diocese diocese, Province province, Chaplain chaplain, Office office, String imagePath) {
        mapCommonFields(dto, diocese, chaplain, office, imagePath);
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
                ChaplainMapper.toListDTO(diocese.getChaplain()),
                OfficeMapper.toListDTO(diocese.getOffice()),
                diocese.getBishop(),
                diocese.getRetiredBishop(),
                diocese.getType(),
                diocese.getProvince().getName()
        );
    }
}
