package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.DioceseCreateDTO;
import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.mappers.DioceseMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.DioceseRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.specifications.DioceseSpecification;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DioceseService extends EcclesiasticalUnitService<Diocese> {
    private final DioceseRepository dioceseRepository;
    private final ProvinceService provinceService;

    public DioceseService(
            DioceseRepository dioceseRepository,
            ProvinceService provinceService,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService
    ) {
        super(dioceseRepository, mediaService, chaplainRepository, officeRepository);
        this.dioceseRepository = dioceseRepository;
        this.provinceService = provinceService;
    }

    @Override
    protected String entityLabel() {
        return Diocese.class.getSimpleName().toUpperCase();
    }

    @Override
    protected String imageSubdirectory() {
        return "dioceses";
    }

    public DioceseListDTO create(DioceseCreateDTO dto) throws IOException {
        validateUniqueName(dto.getName());
        Chaplain chaplain = getChaplainOrThrow(dto.getChaplainId());
        Office office = getOfficeOrThrow(dto.getOfficeId());
        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());
        Province province = provinceService.findByIdOrThrow(dto.getProvinceId());

        Diocese diocese = DioceseMapper.fromCreateDTO(dto, new Diocese(), province, chaplain, office, imagePath);
        Diocese saved = dioceseRepository.save(diocese);

        log.info("{} : {} (ID : {}) créé avec succès !", entityLabel(), saved.getName(), saved.getId());
        return DioceseMapper.toListDTO(saved);
    }

    public DioceseListDTO findById(int id) {
        Diocese found = findByIdOrThrow(id);
        return DioceseMapper.toListDTO(found);
    }

    public List<DioceseListDTO> find(String query) {
        Specification<@NonNull Diocese> spec = DioceseSpecification.globalSearch(query);
        return dioceseRepository.findAll(spec)
                .stream()
                .map(DioceseMapper::toListDTO)
                .toList();
    }

    public List<DioceseListDTO> listAll() {
        return dioceseRepository.findAll()
                .stream()
                .map(DioceseMapper::toListDTO)
                .toList();
    }

    public DioceseListDTO update(int id, DioceseCreateDTO dto) throws IOException {
        Diocese existing = findByIdOrThrow(id);
        if (!existing.getName().equals(dto.getName())) validateUniqueName(dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = (existing.getChaplain() == null || !existing.getChaplain().getId().equals(dto.getChaplainId()))
                ? getChaplainOrThrow(dto.getChaplainId())
                : existing.getChaplain();
        Office office = (existing.getOffice() == null || !existing.getOffice().getId().equals(dto.getChaplainId()))
                ? getOfficeOrThrow(dto.getOfficeId())
                : existing.getOffice();
        Province province = (!existing.getProvince().getId().equals(dto.getProvinceId()))
                ? provinceService.findByIdOrThrow(dto.getProvinceId())
                : existing.getProvince();

        Diocese diocese = DioceseMapper.fromCreateDTO(dto, existing, province, chaplain, office, newImagePath);
        Diocese saved = dioceseRepository.save(diocese);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mis à jour avec succès.", entityLabel(), saved.getName(), id);

        return DioceseMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Diocese found = findByIdOrThrow(id);
        dioceseRepository.delete(found);
    }
}
