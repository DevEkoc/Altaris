package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ParishCreateDTO;
import com.devekoc.altaris.dto.ParishListDTO;
import com.devekoc.altaris.entities.*;
import com.devekoc.altaris.enumerations.ParishType;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParishServiceTest {

    @Mock
    private ParishRepository parishRepository;

    @Mock
    private ZoneService zoneService;

    @Mock
    private ChaplainRepository chaplainRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private MediaService mediaService;

    @InjectMocks
    private ParishService parishService;

    private Parish parish;
    private ParishCreateDTO dto;
    private Zone zone;
    private Chaplain chaplain;
    private Office office;
    private MultipartFile image;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId(1);
        zone.setName("Zone Centre");

        chaplain = new Chaplain();
        chaplain.setId(1);

        office = new Office();
        office.setId(1);

        parish = new Parish();
        parish.setId(1);
        parish.setName("Paroisse Test");
        parish.setZone(zone);
        parish.setPriest("Curé Test");
        parish.setType(ParishType.PAROISSE);
        parish.setServantList(List.of());

        parish.setChaplain(chaplain);
        parish.setOffice(office);

        image = mock(MultipartFile.class);

        dto = new ParishCreateDTO(
                "Paroisse Test",
                "Description",
                "Saint Patron",
                image,
                "Yaoundé",
                1,
                1,
                "Curé Test",
                ParishType.PAROISSE,
                1
        );
    }

    // -------------------- CREATE --------------------

    @Test
    void create_shouldSucceed() throws IOException {
        when(parishRepository.existsByName(anyString())).thenReturn(false);
        when(chaplainRepository.findById(1)).thenReturn(Optional.of(chaplain));
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));
        when(zoneService.findByIdOrThrow(1)).thenReturn(zone);
        when(mediaService.saveImage(any(), anyString())).thenReturn("image.jpg");
        when(parishRepository.save(any())).thenReturn(parish);

        ParishListDTO result = parishService.create(dto);

        assertNotNull(result);
        verify(parishRepository).save(any(Parish.class));
    }

    @Test
    void create_shouldFail_whenNameAlreadyExists() {
        when(parishRepository.existsByName("Paroisse Test")).thenReturn(true);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> parishService.create(dto)
        );
    }

    // -------------------- FIND BY ID --------------------

    @Test
    void findById_shouldReturnDTO() {
        when(parishRepository.findById(1)).thenReturn(Optional.of(parish));

        ParishListDTO result = parishService.findById(1);

        assertNotNull(result);
        assertEquals("Paroisse Test", result.name());
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(parishRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parishService.findById(1)
        );
    }

    // -------------------- SEARCH --------------------

    @Test
    void find_shouldReturnList() {
        when(parishRepository.findAll(any(Specification.class))).thenReturn(List.of(parish));

        List<ParishListDTO> result = parishService.find("test");

        assertEquals(1, result.size());
    }

    // -------------------- LIST ALL --------------------

    @Test
    void listAll_shouldReturnList() {
        when(parishRepository.findAll()).thenReturn(List.of(parish));

        List<ParishListDTO> result = parishService.listAll();

        assertEquals(1, result.size());
    }

    // -------------------- UPDATE --------------------

    @Test
    void update_shouldSucceed_withoutImageChange() throws IOException {
        when(parishRepository.findById(1)).thenReturn(Optional.of(parish));
        when(parishRepository.existsByName(anyString())).thenReturn(false);
        when(zoneService.findByIdOrThrow(1)).thenReturn(zone);
        when(parishRepository.save(any())).thenReturn(parish);

        ParishListDTO result = parishService.update(1, dto);

        assertNotNull(result);
        verify(mediaService, never()).deleteImage(anyString());
    }


    @Test
    void update_shouldFail_whenNewNameAlreadyExists() {
        ParishCreateDTO dtoWithNewName = new ParishCreateDTO(
                "Nouveau Nom",
                dto.getDescription(),
                dto.getSaintPatron(),
                dto.getImage(),
                dto.getLocality(),
                dto.getChaplainId(),
                dto.getOfficeId(),
                dto.getPriest(),
                dto.getType(),
                dto.getZoneId()
        );

        when(parishRepository.findById(1)).thenReturn(Optional.of(parish));
        when(parishRepository.existsByName("Nouveau Nom")).thenReturn(true);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> parishService.update(1, dtoWithNewName)
        );
    }


    // -------------------- DELETE --------------------

    @Test
    void delete_shouldSucceed() {
        parish.setImage("image.png");
        when(parishRepository.findById(1)).thenReturn(Optional.of(parish));

        parishService.delete(1);

        verify(parishRepository).delete(parish);
        verify(mediaService).deleteImage("image.png");
    }

    @Test
    void delete_shouldFail_whenServantsExist() {
        parish.setServantList(List.of(mock(Servant.class)));
        when(parishRepository.findById(1)).thenReturn(Optional.of(parish));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> parishService.delete(1)
        );
    }
}
