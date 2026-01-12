package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.dioceses.DioceseCreateDTO;
import com.devekoc.altaris.dto.dioceses.DioceseDetailsDTO;
import com.devekoc.altaris.dto.dioceses.DioceseListDTO;
import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.mappers.DioceseMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.DioceseRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.specifications.DioceseSpecification;
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
public class DioceseService extends EcclesiasticalUnitService<Diocese> {
    private final DioceseRepository dioceseRepository;
    private final ProvinceService provinceService;
    private final OfficeService officeService;

    public DioceseService(
            DioceseRepository dioceseRepository,
            ProvinceService provinceService,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService,
            OfficeService officeService
    ) {
        super(dioceseRepository, mediaService, chaplainRepository, officeRepository);
        this.dioceseRepository = dioceseRepository;
        this.provinceService = provinceService;
        this.officeService = officeService;
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
        Chaplain chaplain = (dto.getChaplainId() == null)
                ? null
                : getChaplainOrThrow(dto.getChaplainId());

        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());
        Province province = provinceService.findByIdOrThrow(dto.getProvinceId());

        Diocese diocese = DioceseMapper.fromCreateDTO(dto, new Diocese(), province, chaplain, imagePath);
        Diocese saved = dioceseRepository.save(diocese);

        log.info("{} : {} (ID : {}) créé avec succès !", entityLabel(), saved.getName(), saved.getId());
        return DioceseMapper.toListDTO(saved);
    }

    public DioceseDetailsDTO findById(int id) {
        Diocese found = findByIdOrThrow(id);
        OfficeListDTO officeListDTO = null;

        if (officeService.existsByActiveUnitId(found.getId())) {
            officeListDTO = officeService.findByUnitId(found.getId());
        }

        return DioceseMapper.toDetailsDTO(found, officeListDTO);
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
        checkNewNameValidity(existing.getName(), dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = resolveChaplain(existing.getChaplain(), dto.getChaplainId());

        Province province = (!existing.getProvince().getId().equals(dto.getProvinceId()))
                ? provinceService.findByIdOrThrow(dto.getProvinceId())
                : existing.getProvince();

        Diocese diocese = DioceseMapper.fromCreateDTO(dto, existing, province, chaplain, newImagePath);
        Diocese saved = dioceseRepository.save(diocese);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mis à jour avec succès.", entityLabel(), saved.getName(), id);

        return DioceseMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Diocese found = findByIdOrThrow(id);
        if (!found.getZoneList().isEmpty()) {
            throw new DataIntegrityViolationException("Impossible de supprimer un Diocèse contenant des Zones !");
        }
        mediaService.deleteImage(found.getImage());
        dioceseRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimé avec succès.", entityLabel(), found.getName(), found.getId());
    }
}
