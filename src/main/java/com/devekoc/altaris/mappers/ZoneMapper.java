package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.ZoneCreateDTO;
import com.devekoc.altaris.dto.ZoneListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Zone;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Diocese;

public final class ZoneMapper extends EcclesiasticalUnitMapper {
    public static Zone fromCreateDTO (ZoneCreateDTO dto, Zone zone, Diocese diocese, Chaplain chaplain, Office office, String imagePath) {
        mapCommonFields(dto, zone, chaplain, office, imagePath);
        zone.setEpiscopalVicar(dto.getEpiscopalVicar());
        zone.setDiocese(diocese);
        return zone;
    }

    public static ZoneListDTO toListDTO(Zone zone) {
        return new ZoneListDTO(
                zone.getId(),
                zone.getName(),
                zone.getDescription(),
                zone.getSaintPatron(),
                zone.getImage(),
                zone.getLocality(),
                ChaplainMapper.toListDTO(zone.getChaplain()),
                OfficeMapper.toListDTO(zone.getOffice()),
                zone.getEpiscopalVicar(),
                zone.getDiocese().getName()
        );
    }
}
