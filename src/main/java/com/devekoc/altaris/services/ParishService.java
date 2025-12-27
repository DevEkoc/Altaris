package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ParishCreateDTO;
import com.devekoc.altaris.dto.ParishListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Zone;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Parish;
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

    public ParishService(
            ParishRepository parishRepository,
            ZoneService zoneService,
            ChaplainRepository chaplainRepository,
            OfficeRepository officeRepository,
            MediaService mediaService
    ) {
        super(parishRepository, mediaService, chaplainRepository, officeRepository);
        this.parishRepository = parishRepository;
        this.zoneService = zoneService;
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
        Chaplain chaplain = getChaplainOrThrow(dto.getChaplainId());
        Office office = getOfficeOrThrow(dto.getOfficeId());
        String imagePath = mediaService.saveImage(dto.getImage(), imageSubdirectory());
        Zone zone = zoneService.findByIdOrThrow(dto.getZoneId());

        Parish parish = ParishMapper.fromCreateDTO(dto, new Parish(), zone, chaplain, office, imagePath);
        Parish saved = parishRepository.save(parish);

        log.info("{} : {} (ID : {}) créée avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ParishMapper.toListDTO(saved);
    }

    public ParishListDTO findById(int id) {
        Parish found = findByIdOrThrow(id);
        return ParishMapper.toListDTO(found);
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
        if (!existing.getName().equals(dto.getName())) validateUniqueName(dto.getName());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.getImage(), oldImagePath);

        Chaplain chaplain = (existing.getChaplain() == null || !existing.getChaplain().getId().equals(dto.getChaplainId()))
                ? getChaplainOrThrow(dto.getChaplainId())
                : existing.getChaplain();
        Office office = (existing.getOffice() == null || !existing.getOffice().getId().equals(dto.getChaplainId()))
                ? getOfficeOrThrow(dto.getOfficeId())
                : existing.getOffice();
        Zone zone = (!existing.getZone().getId().equals(dto.getZoneId()))
                ? zoneService.findByIdOrThrow(dto.getZoneId())
                : existing.getZone();

        Parish parish = ParishMapper.fromCreateDTO(dto, existing, zone, chaplain, office, newImagePath);
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
