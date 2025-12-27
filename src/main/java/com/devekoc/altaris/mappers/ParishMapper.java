package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.ParishCreateDTO;
import com.devekoc.altaris.dto.ParishListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Zone;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Parish;

public final class ParishMapper extends EcclesiasticalUnitMapper {
    public static Parish fromCreateDTO (ParishCreateDTO dto, Parish parish, Zone zone, Chaplain chaplain, Office office, String imagePath) {
        mapCommonFields(dto, parish, chaplain, office, imagePath);
        parish.setPriest(dto.getPriest());
        parish.setType(dto.getType());
        parish.setZone(zone);
        return parish;
    }

    public static ParishListDTO toListDTO(Parish parish) {
        return new ParishListDTO(
                parish.getId(),
                parish.getName(),
                parish.getDescription(),
                parish.getSaintPatron(),
                parish.getImage(),
                parish.getLocality(),
                ChaplainMapper.toListDTO(parish.getChaplain()),
                OfficeMapper.toListDTO(parish.getOffice()),
                parish.getPriest(),
                parish.getType(),
                parish.getZone().getName()
        );
    }
}
