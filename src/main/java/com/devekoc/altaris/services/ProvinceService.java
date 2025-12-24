package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ProvinceCreateDTO;
import com.devekoc.altaris.dto.ProvinceListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.mappers.ProvinceMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.repositories.ProvinceRepository;
import com.devekoc.altaris.specifications.ProvinceSpecification;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ProvinceService extends EcclesiasticalUnitService<Province> {
    private final ProvinceRepository provinceRepository;

    public ProvinceService(
            ProvinceRepository provinceRepository,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService
    ) {
        super(provinceRepository, mediaService, chaplainRepository, officeRepository);
        this.provinceRepository = provinceRepository;
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
        Chaplain chaplain = getChaplainOrThrow(dto.getChaplainId());
        Office office = getOfficeOrThrow(dto.getOfficeId());
        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());

        Province province = ProvinceMapper.fromCreateDTO(dto, new Province(), chaplain, office, imagePath);
        Province saved = provinceRepository.save(province);

        return ProvinceMapper.toListDTO(saved);
    }

    public ProvinceListDTO findById(int id) {
        Province found = findByIdOrThrow(id);
        return ProvinceMapper.toListDTO(found);
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
                .map(
                        ProvinceMapper::toListDTO
                )
                .toList();
    }

    public ProvinceListDTO update(int id, ProvinceCreateDTO dto) throws IOException {
        Province existing = findByIdOrThrow(id);

        if (!existing.getName().equals(dto.getName())) validateUniqueName(dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = (existing.getChaplain() == null || !existing.getChaplain().getId().equals(dto.getChaplainId()))
                ? getChaplainOrThrow(dto.getChaplainId())
                : existing.getChaplain();
        Office office = (existing.getOffice() == null || !existing.getOffice().getId().equals(dto.getChaplainId()))
                ? getOfficeOrThrow(dto.getOfficeId())
                : existing.getOffice();

        ProvinceMapper.fromCreateDTO(dto, existing, chaplain, office, newImagePath);
        Province saved = provinceRepository.save(existing);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("Province '{}' (ID: {}) mise à jour avec succès.", saved.getName(), id);
        return ProvinceMapper.toListDTO(saved);
    }


    public void delete(int id) {
        Province found = findByIdOrThrow(id);
        provinceRepository.delete(found);
    }
}
