package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.offices.OfficeListDTO;
import com.devekoc.altaris.dto.parishes.ParishCreateDTO;
import com.devekoc.altaris.dto.parishes.ParishDetailsDTO;
import com.devekoc.altaris.dto.parishes.ParishListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Parish;
import com.devekoc.altaris.entities.Zone;
import com.devekoc.altaris.mappers.ParishMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.repositories.ParishRepository;
import com.devekoc.altaris.specifications.ParishSpecification;
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
public class ParishService extends EcclesiasticalUnitService<Parish> {
    private final ParishRepository parishRepository;
    private final ZoneService zoneService;
    private final OfficeService officeService;

    public ParishService(
            ParishRepository parishRepository,
            ZoneService zoneService,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService,
            OfficeService officeService
    ) {
        super(parishRepository, mediaService, chaplainRepository, officeRepository);
        this.parishRepository = parishRepository;
        this.zoneService = zoneService;
        this.officeService = officeService;
    }

    @Override
    protected String entityLabel() {
        return Parish.class.getSimpleName().toUpperCase();
    }

    @Override
    protected String imageSubdirectory() {
        return "parishes";
    }

    public ParishListDTO create(ParishCreateDTO dto) throws IOException {
        validateUniqueName(dto.getName());
        Chaplain chaplain = (dto.getChaplainId() == null)
                ? null
                : getChaplainOrThrow(dto.getChaplainId());

        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());
        Zone zone = zoneService.findByIdOrThrow(dto.getZoneId());

        Parish parish = ParishMapper.fromCreateDTO(dto, new Parish(), zone, chaplain, imagePath);
        Parish saved = parishRepository.save(parish);

        log.info("{} : {} (ID : {}) créée avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ParishMapper.toListDTO(saved);
    }

    public ParishDetailsDTO findById(int id) {
        Parish found = findByIdOrThrow(id);
        OfficeListDTO officeListDTO = null;

        if (officeService.existsByActiveUnitId(found.getId())) {
            officeListDTO = officeService.findByUnitId(found.getId());
        }

        return ParishMapper.toDetailsDTO(found, officeListDTO);
    }

    public List<ParishListDTO> find(String query) {
        Specification<@NonNull Parish> spec = ParishSpecification.globalSearch(query);
        return parishRepository.findAll(spec)
                .stream()
                .map(ParishMapper::toListDTO)
                .toList();
    }

    public List<ParishListDTO> listAll() {
        return parishRepository.findAll()
                .stream()
                .map(ParishMapper::toListDTO)
                .toList();
    }

    public ParishListDTO update(int id, ParishCreateDTO dto) throws IOException {
        Parish existing = findByIdOrThrow(id);
        checkNewNameValidity(existing.getName(), dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = resolveChaplain(existing.getChaplain(), dto.getChaplainId());

        Zone zone = (!existing.getZone().getId().equals(dto.getZoneId()))
                ? zoneService.findByIdOrThrow(dto.getZoneId())
                : existing.getZone();

        Parish parish = ParishMapper.fromCreateDTO(dto, existing, zone, chaplain, newImagePath);
        Parish saved = parishRepository.save(parish);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mise à jour avec succès.", entityLabel(), saved.getName(), id);

        return ParishMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Parish found = findByIdOrThrow(id);
        if (!found.getServantList().isEmpty()) {
            throw new DataIntegrityViolationException("Impossible de supprimer une Paroisse contenant des Servants !");
        }
        mediaService.deleteImage(found.getImage());
        parishRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimée avec succès.", entityLabel(), found.getName(), found.getId());
    }
}
