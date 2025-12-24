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

    protected Office getOfficeOrThrow(Integer officeId) {
        if (officeId == null) return null;

        return officeRepository.findById(officeId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Bureau introuvable avec l'ID '%d' !", officeId)
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
