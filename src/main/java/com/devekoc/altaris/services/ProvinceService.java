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
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final ChaplainRepository chaplainRepository;
    private final OfficeRepository officeRepository;
    private final MediaService mediaService;
    private static final String IMAGE_SUBDIRECTORY = "provinces";


    public ProvinceListDTO create(ProvinceCreateDTO dto) throws IOException {
        validateUniqueName(dto.name());
        Chaplain chaplain = getChaplainOrThrow(dto.chaplainId());
        Office office = getOfficeOrThrow(dto.officeId());
        String imagePath = mediaService.saveImage(dto.image(), IMAGE_SUBDIRECTORY);

        Province province = ProvinceMapper.fromCreateDTO(dto, new Province(), chaplain, office, imagePath);
        Province saved = provinceRepository.save(province);

        return ProvinceMapper.toListDTO(saved);
    }

    public ProvinceListDTO findById(int id) {
        Province found = findProvinceById(id);
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
        Province existing = findProvinceById(id);

        if (!existing.getName().equals(dto.name())) validateUniqueName(dto.name());

        String oldImagePath = existing.getImage();
        String newImagePath = handleImageUpdate(dto.image(), oldImagePath);

        Chaplain chaplain = (existing.getChaplain() == null || !existing.getChaplain().getId().equals(dto.chaplainId()))
                ? getChaplainOrThrow(dto.chaplainId())
                : existing.getChaplain();
        Office office = (existing.getOffice() == null || !existing.getOffice().getId().equals(dto.officeId()))
                ? getOfficeOrThrow(dto.officeId())
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
        Province found = findProvinceById(id);
        provinceRepository.delete(found);
    }

    private void validateUniqueName(String name) {
        if (provinceRepository.existsByName(name)) {
            throw new DataIntegrityViolationException("Une Province avec le nom " + name + " existe déjà !");
        }
    }

    private Province findProvinceById (int id) {
        return provinceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune Province trouvée avec l'ID " + id)
        );
    }

    /**
     *
     * @param chaplainId
     * @return L'Aumônier en fonction de l'ID
     * @throws EntityNotFoundException si aucun Aumônier ne correspond à l'ID
     */
    private Chaplain getChaplainOrThrow(Integer chaplainId) {
        if (chaplainId != null) {
            return chaplainRepository.findById(chaplainId).orElseThrow(
                    ()-> new EntityNotFoundException("Aucun Aumônier trouvé avec l'ID " + chaplainId)
            );
        }
        return null;
    }

    /**
     *
     * @param officeId
     * @return Le Bureau en fonction de l'ID
     * @throws EntityNotFoundException si aucun Bureau ne correspond à l'ID
     */
    private Office getOfficeOrThrow(Integer officeId) {
        if (officeId != null) {
            return officeRepository.findById(officeId).orElseThrow(
                        ()-> new EntityNotFoundException("Aucun Bureau trouvé avec l'ID " + officeId)
            );
        }
        return null;
    }

    /** Met à jour l’image si nécessaire et renvoie le nouveau chemin */
    private String handleImageUpdate(MultipartFile newImage, String oldPath) throws IOException {
        if (newImage != null && !newImage.isEmpty()) {
            return mediaService.saveImage(newImage, IMAGE_SUBDIRECTORY);
        }
        return oldPath; // garde l’ancienne si aucune nouvelle image
    }
}
