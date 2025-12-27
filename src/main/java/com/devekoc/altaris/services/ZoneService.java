package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ZoneCreateDTO;
import com.devekoc.altaris.dto.ZoneListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Zone;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.mappers.ZoneMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.ZoneRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.specifications.ZoneSpecification;
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
public class ZoneService extends EcclesiasticalUnitService<Zone> {
    private final ZoneRepository zoneRepository;
    private final DioceseService dioceseService;

    public ZoneService(
            ZoneRepository zoneRepository,
            DioceseService dioceseService,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService
    ) {
        super(zoneRepository, mediaService, chaplainRepository, officeRepository);
        this.zoneRepository = zoneRepository;
        this.dioceseService = dioceseService;
    }

    @Override
    protected String entityLabel() {
        return Zone.class.getSimpleName().toUpperCase();
    }

    @Override
    protected String imageSubdirectory() {
        return "zones";
    }

    public ZoneListDTO create(ZoneCreateDTO dto) throws IOException {
        validateUniqueName(dto.getName());
        Chaplain chaplain = getChaplainOrThrow(dto.getChaplainId());
        Office office = getOfficeOrThrow(dto.getOfficeId());
        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());
        Diocese diocese = dioceseService.findByIdOrThrow(dto.getDioceseId());

        Zone zone = ZoneMapper.fromCreateDTO(dto, new Zone(), diocese, chaplain, office, imagePath);
        Zone saved = zoneRepository.save(zone);

        log.info("{} : {} (ID : {}) créée avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ZoneMapper.toListDTO(saved);
    }

    public ZoneListDTO findById(int id) {
        Zone found = findByIdOrThrow(id);
        return ZoneMapper.toListDTO(found);
    }

    public List<ZoneListDTO> find(String query) {
        Specification<@NonNull Zone> spec = ZoneSpecification.globalSearch(query);
        return zoneRepository.findAll(spec)
                .stream()
                .map(ZoneMapper::toListDTO)
                .toList();
    }

    public List<ZoneListDTO> listAll() {
        return zoneRepository.findAll()
                .stream()
                .map(ZoneMapper::toListDTO)
                .toList();
    }

    public ZoneListDTO update(int id, ZoneCreateDTO dto) throws IOException {
        Zone existing = findByIdOrThrow(id);
        if (!existing.getName().equals(dto.getName())) validateUniqueName(dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = (existing.getChaplain() == null || !existing.getChaplain().getId().equals(dto.getChaplainId()))
                ? getChaplainOrThrow(dto.getChaplainId())
                : existing.getChaplain();
        Office office = (existing.getOffice() == null || !existing.getOffice().getId().equals(dto.getChaplainId()))
                ? getOfficeOrThrow(dto.getOfficeId())
                : existing.getOffice();
        Diocese diocese = (!existing.getDiocese().getId().equals(dto.getDioceseId()))
                ? dioceseService.findByIdOrThrow(dto.getDioceseId())
                : existing.getDiocese();

        Zone zone = ZoneMapper.fromCreateDTO(dto, existing, diocese, chaplain, office, newImagePath);
        Zone saved = zoneRepository.save(zone);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mise à jour avec succès.", entityLabel(), saved.getName(), id);

        return ZoneMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Zone found = findByIdOrThrow(id);
        if (!found.getParishList().isEmpty()) {
            throw new DataIntegrityViolationException("Impossible de supprimer une Zone contenant des Paroisses !");
        }
        mediaService.deleteImage(found.getImage());
        zoneRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimée avec succès.", entityLabel(), found.getName(), found.getId());
    }
}
