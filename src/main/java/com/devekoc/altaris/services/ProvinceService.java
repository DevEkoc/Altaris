package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.dto.provinces.ProvinceCreateDTO;
import com.devekoc.altaris.dto.provinces.ProvinceDetailsDTO;
import com.devekoc.altaris.dto.provinces.ProvinceListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.mappers.ProvinceMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.repositories.ProvinceRepository;
import com.devekoc.altaris.specifications.ProvinceSpecification;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ProvinceService extends EcclesiasticalUnitService<Province> {
    private final ProvinceRepository provinceRepository;
    private final OfficeService officeService;

    public ProvinceService(
            ProvinceRepository provinceRepository,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService,
            OfficeService officeService
    ) {
        super(provinceRepository, mediaService, chaplainRepository, officeRepository);
        this.provinceRepository = provinceRepository;
        this.officeService = officeService;
    }

    @Override
    protected String entityLabel() {
        return Province.class.getSimpleName().toUpperCase();
    }

    @Override
    protected String imageSubdirectory() {
        return "provinces";
    }



    public ProvinceListDTO create(ProvinceCreateDTO dto) throws IOException {
        validateUniqueName(dto.getName());
        Chaplain chaplain = (dto.getChaplainId() == null)
            ? null
            : getChaplainOrThrow(dto.getChaplainId());

        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());

        Province province = ProvinceMapper.fromCreateDTO(dto, new Province(), chaplain, imagePath);
        Province saved = provinceRepository.save(province);

        log.info("{} : {} (ID : {}) créée avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ProvinceMapper.toListDTO(saved);
    }

    public ProvinceDetailsDTO findById(int id) {
        Province found = findByIdOrThrow(id);
        OfficeListDTO officeListDTO = null;

        if (officeService.existsByActiveUnitId(found.getId())) {
            officeListDTO = officeService.findByUnitId(found.getId());
        }

        return ProvinceMapper.toDetailsDTO(found, officeListDTO);
    }

    public List<ProvinceListDTO> listAll() {
        return provinceRepository.findAll()
                .stream()
                .map(ProvinceMapper::toListDTO)
                .toList();
    }

    public List<ProvinceListDTO> find(String query) {
        Specification<@NonNull Province> spec = ProvinceSpecification.globalSearch(query);
        return provinceRepository.findAll(spec)
                .stream()
                .map(ProvinceMapper::toListDTO)
                .toList();
    }

    public ProvinceListDTO update(int id, ProvinceCreateDTO dto) throws IOException {
        Province existing = findByIdOrThrow(id);

        checkNewNameValidity(existing.getName(), dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = resolveChaplain(existing.getChaplain(), dto.getChaplainId());

        ProvinceMapper.fromCreateDTO(dto, existing, chaplain, newImagePath);
        Province saved = provinceRepository.save(existing);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mise à jour avec succès.", entityLabel(), saved.getName(), id);
        return ProvinceMapper.toListDTO(saved);
    }


    public void delete(int id) {
        Province found = findByIdOrThrow(id);
        if (!found.getDioceseList().isEmpty()) {
            throw new DataIntegrityViolationException("Impossible de supprimer une Province contenant des Diocèses !");
        }
        mediaService.deleteImage(found.getImage());
        provinceRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimée avec succès.", entityLabel(), found.getName(), found.getId());
    }
}
