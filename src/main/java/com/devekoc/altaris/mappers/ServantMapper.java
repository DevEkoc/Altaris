package com.devekoc.altaris.mappers;

import com.devekoc.altaris.dto.ServantCreateDTO;
import com.devekoc.altaris.dto.ServantListDTO;
import com.devekoc.altaris.entities.Servant;
import com.devekoc.altaris.entities.Parish;

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
                servant.getParish().getName()
        );
    }
}
