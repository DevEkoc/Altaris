package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ZoneCreateDTO;
import com.devekoc.altaris.dto.ZoneListDTO;
import com.devekoc.altaris.entities.*;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;
    @Mock
    private DioceseService dioceseService;
    @Mock
    private MediaService mediaService;

    @InjectMocks
    private ZoneService zoneService;

    private Zone zone;
    private Diocese diocese;
    private ZoneCreateDTO dto;

    @BeforeEach
    void setup() {
        diocese = new Diocese();
        diocese.setId(1);
        diocese.setName("Diocèse Centre");

        zone = new Zone();
        zone.setId(1);
        zone.setName("Zone Centre");
        zone.setDiocese(diocese);
        zone.setParishList(List.of());

        dto = new ZoneCreateDTO(
                "Zone Centre",
                "Description",
                "Saint Paul",
                null,
                "Yaoundé",
                null,
                null,
                "Vicaire X",
                1
        );
    }

    /* ======================================================
       CREATE
       ====================================================== */

    @Test
    void create_shouldSucceed() throws IOException {
        when(zoneRepository.existsByName(dto.getName())).thenReturn(false);
        when(dioceseService.findByIdOrThrow(1)).thenReturn(diocese);
        when(mediaService.saveImage(null, "zones")).thenReturn("zone.png");
        when(zoneRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ZoneListDTO result = zoneService.create(dto);

        assertEquals("Zone Centre", result.name());
        verify(zoneRepository).save(any());
    }

    @Test
    void create_shouldFail_whenNameExists() {
        when(zoneRepository.existsByName(dto.getName())).thenReturn(true);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> zoneService.create(dto)
        );
    }

    @Test
    void create_shouldFail_whenDioceseNotFound() {
        when(zoneRepository.existsByName(dto.getName())).thenReturn(false);
        when(dioceseService.findByIdOrThrow(1))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> zoneService.create(dto)
        );
    }

    /* ======================================================
       FIND
       ====================================================== */

    @Test
    void findById_shouldReturnDTO() {
        when(zoneRepository.findById(1)).thenReturn(Optional.of(zone));

        ZoneListDTO result = zoneService.findById(1);

        assertEquals("Zone Centre", result.name());
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(zoneRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> zoneService.findById(1)
        );
    }

    @Test
    void listAll_shouldReturnList() {
        when(zoneRepository.findAll()).thenReturn(List.of(zone));

        List<ZoneListDTO> result = zoneService.listAll();

        assertEquals(1, result.size());
    }

    @Test
    void search_shouldReturnList() {
        when(zoneRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(zone));
        List<ZoneListDTO> result = zoneService.find("centre");
        assertEquals(1, result.size());
    }

    /* ======================================================
       UPDATE
       ====================================================== */

    @Test
    void update_shouldReplaceImage() throws IOException {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.png", "image/png", "fake".getBytes()
        );

        ZoneCreateDTO updateDto = new ZoneCreateDTO(
                "Zone Centre",
                "Desc",
                null,
                image,
                null,
                null,
                null,
                "Vicaire Y",
                1
        );

        zone.setImage("old.png");

        when(zoneRepository.findById(1)).thenReturn(Optional.of(zone));
        when(mediaService.saveImage(image, "zones")).thenReturn("new.png");
        when(zoneRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ZoneListDTO result = zoneService.update(1, updateDto);

        assertEquals("new.png", result.image());
        verify(mediaService).deleteImage("old.png");
    }

    @Test
    void update_shouldFail_whenNewNameExists() {
        when(zoneRepository.findById(1)).thenReturn(Optional.of(zone));
        when(zoneRepository.existsByName("Autre")).thenReturn(true);

        ZoneCreateDTO updateDto = new ZoneCreateDTO(
                "Autre",
                "Desc",
                null,
                null,
                null,
                null,
                null,
                "Vicaire",
                1
        );

        assertThrows(
                DataIntegrityViolationException.class,
                () -> zoneService.update(1, updateDto)
        );
    }

    /* ======================================================
       DELETE
       ====================================================== */

    @Test
    void delete_shouldSucceed_whenNoParishes() {
        when(zoneRepository.findById(1)).thenReturn(Optional.of(zone));

        zoneService.delete(1);

        verify(zoneRepository).delete(zone);
        verify(mediaService).deleteImage(zone.getImage());
    }

    @Test
    void delete_shouldFail_whenParishesExist() {
        zone.setParishList(List.of(mock(Parish.class)));
        when(zoneRepository.findById(1)).thenReturn(Optional.of(zone));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> zoneService.delete(1)
        );
    }
}

