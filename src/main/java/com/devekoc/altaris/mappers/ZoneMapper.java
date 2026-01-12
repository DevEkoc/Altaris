package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.dto.zones.ZoneCreateDTO;
import com.devekoc.altaris.dto.zones.ZoneDetailsDTO;
import com.devekoc.altaris.dto.zones.ZoneListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Zone;

public final class ZoneMapper extends EcclesiasticalUnitMapper {
    public static Zone fromCreateDTO (ZoneCreateDTO dto, Zone zone, Diocese diocese, Chaplain chaplain, String imagePath) {
        mapCommonFields(dto, zone, chaplain, imagePath);
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
                zone.getEpiscopalVicar(),
                zone.getDiocese().getId(),
                zone.getDiocese().getName()
        );
    }

    public static ZoneDetailsDTO toDetailsDTO(Zone zone, OfficeListDTO office) {
        return new ZoneDetailsDTO(
                zone.getId(),
                zone.getName(),
                zone.getDescription(),
                zone.getSaintPatron(),
                zone.getImage(),
                zone.getLocality(),
                ChaplainMapper.toListDTO(zone.getChaplain()),
                office,
                zone.getEpiscopalVicar(),
                zone.getDiocese().getId(),
                zone.getDiocese().getName()
        );
    }
}
