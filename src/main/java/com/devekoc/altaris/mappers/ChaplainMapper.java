package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.ChaplainCreateDTO;
import com.devekoc.altaris.dto.ChaplainListDTO;
import com.devekoc.altaris.entities.Chaplain;

public final class ChaplainMapper {

    public static Chaplain fromCreateDTO (ChaplainCreateDTO dto, Chaplain chaplain) {
        chaplain.setName(dto.name());
        chaplain.setSurname(dto.surname());
        chaplain.setPriestlyRank(dto.priestlyRank());
        chaplain.setPhone(dto.phone());
        return chaplain;
    }

    public static ChaplainListDTO toListDTO (Chaplain chaplain) {
        // Le Chaplain null est autorisé lors de la création d'une unité
        if (chaplain == null) return null;

        return new ChaplainListDTO(
            chaplain.getId(),
            chaplain.getName(),
            chaplain.getSurname(),
            chaplain.getPriestlyRank(),
            chaplain.getPhone()
        );
    }
}
