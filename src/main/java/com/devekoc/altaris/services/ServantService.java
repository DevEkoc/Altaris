package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.servants.ServantCreateDTO;
import com.devekoc.altaris.dto.servants.ServantListDTO;
import com.devekoc.altaris.entities.Parish;
import com.devekoc.altaris.entities.Servant;
import com.devekoc.altaris.mappers.ServantMapper;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ServantRepository;
import com.devekoc.altaris.specifications.ServantSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServantService {
    private final ServantRepository servantRepository;
    private final ParishService parishService;
    private final MediaService mediaService;

    protected String entityLabel() {
        return Servant.class.getSimpleName().toUpperCase();
    }

    protected String imageSubdirectory() {
        return "servants";
    }

    public ServantListDTO create(ServantCreateDTO dto) throws IOException {

        String imagePath = mediaService.saveImage(dto.image(), imageSubdirectory());
        Parish parish = parishService.findByIdOrThrow(dto.parishId());

        Servant servant = ServantMapper.fromCreateDTO(dto, new Servant(), parish, imagePath);
        Servant saved = servantRepository.save(servant);

        String serialNumber = generateSerialNumber(saved.getId());
        saved.setSerialNumber(serialNumber);
        saved = servantRepository.save(saved);

        log.info("{} : {} (ID : {}) créé avec succès !", entityLabel(), saved.getName(), saved.getId());
        return ServantMapper.toListDTO(saved);
    }


    public ServantListDTO findById(int id) {
        Servant found = findByIdOrThrow(id);
        return ServantMapper.toListDTO(found);
    }

    public List<ServantListDTO> find(String query) {
        Specification<@NonNull Servant> spec = ServantSpecification.globalSearch(query);
        return servantRepository.findAll(spec)
                .stream()
                .map(ServantMapper::toListDTO)
                .toList();
    }

    public List<ServantListDTO> listAll() {
        return servantRepository.findAll()
                .stream()
                .map(ServantMapper::toListDTO)
                .toList();
    }

    public ServantListDTO update(int id, ServantCreateDTO dto) throws IOException {
        Servant existing = findByIdOrThrow(id);

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.image(), oldImagePath);

        Parish parish = (!existing.getParish().getId().equals(dto.parishId()))
                ? parishService.findByIdOrThrow(dto.parishId())
                : existing.getParish();

        Servant servant = ServantMapper.fromCreateDTO(dto, existing, parish, newImagePath);
        Servant saved = servantRepository.save(servant);

        if (!Objects.equals(newImagePath, oldImagePath)) {
            mediaService.deleteImage(oldImagePath);
        }

        log.info("{} '{}' (ID: {}) mis à jour avec succès.", entityLabel(), saved.getName(), id);

        return ServantMapper.toListDTO(saved);
    }

    public void delete(int id) {
        Servant found = findByIdOrThrow(id);
        mediaService.deleteImage(found.getImage());
        servantRepository.delete(found);
        log.info("{} '{}' (ID: {}) supprimé avec succès.", entityLabel(), found.getName(), found.getId());
    }


    private String generateSerialNumber(Integer id) {
        int year = LocalDate.now().getYear();
        String serialNumber = String.format("SER%d", year);
        return serialNumber + "%03d".formatted(id);
    }

    public Servant findByIdOrThrow(int id) {
        return servantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID '%d' !", entityLabel(), id)
                )
        );
    }

    private String handleImageUpdate(MultipartFile newImage, String oldPath) throws IOException {
        if (newImage != null && !newImage.isEmpty()) {
            return mediaService.saveImage(newImage, imageSubdirectory());
        }
        return oldPath;
    }
}
