package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ChaplainListDTO;
import com.devekoc.altaris.dto.OfficeListDTO;
import com.devekoc.altaris.dto.ProvinceCreateDTO;
import com.devekoc.altaris.dto.ProvinceListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.mappers.ChaplainMapper;
import com.devekoc.altaris.mappers.OfficeMapper;
import com.devekoc.altaris.mappers.ProvinceMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.repositories.ProvinceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final ChaplainRepository chaplainRepository;
    private final OfficeRepository officeRepository;
    private final MediaService mediaService;
    private static final String IMAGE_SUBDIRECTORY = "provinces";


    public ProvinceListDTO create(ProvinceCreateDTO dto) throws IOException {
        validateUniqueName(dto.name());

        Chaplain chaplain = chaplainRepository.findById(dto.chaplainId()).orElseThrow(
                ()-> new EntityNotFoundException("Aucun Aumônier trouvé avec l'ID " + dto.chaplainId())
        );
        Office office = officeRepository.findById(dto.officeId()).orElseThrow(
                ()-> new EntityNotFoundException("Aucun Bureau trouvé avec l'ID " + dto.officeId())
        );
        String imagePath = mediaService.saveImage(dto.image(), IMAGE_SUBDIRECTORY);

        Province province = ProvinceMapper.fromCreateDTO(dto, new Province(), chaplain, office, imagePath);
        Province saved = provinceRepository.save(province);

        ChaplainListDTO chaplainListDTO = ChaplainMapper.toListDTO(chaplain);
        OfficeListDTO officeListDTO = OfficeMapper.toListDTO(office);
        return ProvinceMapper.toProvinceListDTO(saved,  chaplainListDTO, officeListDTO);
    }

    private void validateUniqueName(String name) {
        if (provinceRepository.existsByName(name)) {
            throw new DataIntegrityViolationException("Une Province avec le nom " + name + " existe déjà !");
        }
    }
}
