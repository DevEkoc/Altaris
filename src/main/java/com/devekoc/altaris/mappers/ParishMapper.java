package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.dto.parishes.ParishCreateDTO;
import com.devekoc.altaris.dto.parishes.ParishDetailsDTO;
import com.devekoc.altaris.dto.parishes.ParishListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Parish;
import com.devekoc.altaris.entities.Zone;

public final class ParishMapper extends EcclesiasticalUnitMapper {
    public static Parish fromCreateDTO (ParishCreateDTO dto, Parish parish, Zone zone, Chaplain chaplain, String imagePath) {
        mapCommonFields(dto, parish, chaplain, imagePath);
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
                parish.getPriest(),
                parish.getType(),
                parish.getZone().getId(),
                parish.getZone().getName()
        );
    }

    public static ParishDetailsDTO toDetailsDTO(Parish parish, OfficeListDTO office) {
        return new ParishDetailsDTO(
                parish.getId(),
                parish.getName(),
                parish.getDescription(),
                parish.getSaintPatron(),
                parish.getImage(),
                parish.getLocality(),
                ChaplainMapper.toListDTO(parish.getChaplain()),
                office,
                parish.getPriest(),
                parish.getType(),
                parish.getZone().getId(),
                parish.getZone().getName()
        );
    }
}
