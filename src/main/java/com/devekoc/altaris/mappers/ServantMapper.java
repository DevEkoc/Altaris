package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.servants.ServantCreateDTO;
import com.devekoc.altaris.dto.servants.ServantListDTO;
import com.devekoc.altaris.entities.Parish;
import com.devekoc.altaris.entities.Servant;

public final class ServantMapper {
    public static Servant fromCreateDTO (ServantCreateDTO dto, Servant servant, Parish parish, String imagePath) {
        servant.setName(dto.name());
        servant.setSurname(dto.surname());
        servant.setBirthDate(dto.birthDate());
        servant.setGender(dto.gender());
        servant.setEntryDate(dto.entryDate());
        servant.setGrade(dto.grade());
        servant.setPhone(dto.phone());
        servant.setImage(imagePath);
        servant.setParish(parish);
        return servant;
    }

    public static ServantListDTO toListDTO(Servant servant) {
        return new ServantListDTO(
                servant.getId(),
                servant.getSerialNumber(),
                servant.getName(),
                servant.getSurname(),
                servant.getBirthDate(),
                servant.getGender(),
                servant.getEntryDate(),
                servant.getGrade(),
                servant.getPhone(),
                servant.getImage(),
                servant.getParish().getId(),
                servant.getParish().getName()
        );
    }
}
