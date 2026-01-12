package com.devekoc.altaris.services;

import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.EcclesiasticalUnitRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class EcclesiasticalUnitService<T> {
    protected final EcclesiasticalUnitRepository<T, Integer> repository;
    protected final MediaService mediaService;
    protected final ChaplainRepository chaplainRepository;
    protected final OfficeRepository officeRepository;

    protected abstract String entityLabel();        // "Province", "Diocèse"…
    protected abstract String imageSubdirectory();  // "provinces", "dioceses"…

    protected void validateUniqueName(String name) {
        if (repository.existsByName(name)) {
            throw new DataIntegrityViolationException(
                    String.format("%s avec le nom '%s' existe déjà !", entityLabel(), name)
            );
        }
    }

    protected void checkNewNameValidity (String oldName, String newName) {
        if (!oldName.equals(newName)) validateUniqueName(newName);
    }

    protected T findByIdOrThrow(int id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("%s introuvable avec l'ID '%d' !", entityLabel(), id)
                )
        );
    }

    protected Chaplain getChaplainOrThrow(Integer chaplainId) {
        if (chaplainId == null) return null;

        return chaplainRepository.findById(chaplainId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Aumônier introuvable avec l'ID '%d' !", chaplainId)
                )
        );
    }

    protected Chaplain resolveChaplain(Chaplain current, Integer newChaplainId) {
        if (newChaplainId == null) {
            return null;
        }

        if (current == null || !current.getId().equals(newChaplainId)) {
            return getChaplainOrThrow(newChaplainId);
        }

        return current;
    }


    protected Office getOfficeOrThrow(Integer unitId) {
        if (unitId == null) return null;

        return officeRepository.findByUnitId(unitId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Bureau introuvable avec l'ID d'unité '%d' !", unitId)
                )
        );
    }

    protected String handleImageUpdate(MultipartFile newImage, String oldPath) throws IOException {
        if (newImage != null && !newImage.isEmpty()) {
            return mediaService.saveImage(newImage, imageSubdirectory());
        }
        return oldPath;
    }
}
