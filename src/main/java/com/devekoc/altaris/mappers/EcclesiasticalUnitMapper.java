package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.EcclesiasticalUnitCreateDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.EcclesiasticalUnit;

public abstract class EcclesiasticalUnitMapper {
    protected static void mapCommonFields(EcclesiasticalUnitCreateDTO dto, EcclesiasticalUnit ecclesiasticalUnit, Chaplain chaplain, String imagePath) {
        ecclesiasticalUnit.setName(dto.getName());
        ecclesiasticalUnit.setDescription(dto.getDescription());
        ecclesiasticalUnit.setSaintPatron(dto.getSaintPatron());
        ecclesiasticalUnit.setImage(imagePath);
        ecclesiasticalUnit.setLocality(dto.getLocality());
        ecclesiasticalUnit.setChaplain(chaplain);
    }
}
