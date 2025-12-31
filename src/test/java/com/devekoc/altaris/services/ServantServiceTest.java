package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ServantCreateDTO;
import com.devekoc.altaris.dto.ServantListDTO;
import com.devekoc.altaris.entities.Parish;
import com.devekoc.altaris.entities.Servant;
import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.devekoc.altaris.medias.MediaService;
import com.devekoc.altaris.repositories.ServantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServantServiceTest {

    @Mock
    private ServantRepository servantRepository;

    @Mock
    private ParishService parishService;

    @Mock
    private MediaService mediaService;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private ServantService servantService;

    private Servant servant;
    private Parish parish;
    private ServantCreateDTO dto;

    @BeforeEach
    void setUp() {
        parish = new Parish();
        parish.setId(1);
        parish.setName("Paroisse Centrale");

        servant = new Servant();
        servant.setId(1);
        servant.setName("Jean");
        servant.setSurname("Dupont");
        servant.setBirthDate(LocalDate.of(2005, 5, 10));
        servant.setGender(Gender.MASCULIN);
        servant.setEntryDate(LocalDate.of(2020, 1, 1));
        servant.setGrade(ServantGrade.ACOLYTE);
        servant.setPhone("699999999");
        servant.setImage("old/path.png");
        servant.setParish(parish);

        dto = new ServantCreateDTO(
                "Jean",
                "Dupont",
                LocalDate.of(2005, 5, 10),
                Gender.MASCULIN,
                LocalDate.of(2020, 1, 1),
                ServantGrade.ACOLYTE,
                "699999999",
                image,
                1
        );
    }

    // ---------------- CREATE ----------------

    @Test
    void create_shouldSucceed() throws IOException {
        when(mediaService.saveImage(image, "servants")).thenReturn("new/path.png");
        when(parishService.findByIdOrThrow(1)).thenReturn(parish);
        when(servantRepository.save(any(Servant.class)))
                .thenAnswer(invocation -> {
                    Servant s = invocation.getArgument(0);
                    s.setId(1);
                    return s;
                });

        ServantListDTO result = servantService.create(dto);

        assertNotNull(result);
        assertEquals("Jean", result.name());
        assertNotNull(result.serialNumber());

        verify(servantRepository, times(2)).save(any());
    }

    // ---------------- FIND BY ID ----------------

    @Test
    void findById_shouldReturnServant() {
        when(servantRepository.findById(1)).thenReturn(Optional.of(servant));

        ServantListDTO result = servantService.findById(1);

        assertEquals("Jean", result.name());
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(servantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> servantService.findById(1));
    }

    // ---------------- FIND (SEARCH) ----------------

    @Test
    void find_shouldReturnList() {
        when(servantRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(servant));

        List<ServantListDTO> result = servantService.find("jean");

        assertEquals(1, result.size());
    }

    // ---------------- LIST ALL ----------------

    @Test
    void listAll_shouldReturnAll() {
        when(servantRepository.findAll()).thenReturn(List.of(servant));

        List<ServantListDTO> result = servantService.listAll();

        assertEquals(1, result.size());
    }

    // ---------------- UPDATE ----------------

    @Test
    void update_shouldSucceed_withoutImageChange() throws IOException {
        ServantCreateDTO dtoWithoutImage = new ServantCreateDTO(
                "Jean",
                "Dupont",
                LocalDate.of(2005, 5, 10),
                Gender.MASCULIN,
                LocalDate.of(2020, 1, 1),
                ServantGrade.ACOLYTE,
                "699999999",
                null,
                1
        );

        when(servantRepository.findById(1)).thenReturn(Optional.of(servant));
        when(servantRepository.save(any())).thenReturn(servant);

        ServantListDTO result = servantService.update(1, dtoWithoutImage);

        assertNotNull(result);
        verify(mediaService, never()).deleteImage(anyString());
        verify(mediaService, never()).saveImage(any(), anyString());
    }


    @Test
    void update_shouldSucceed_withImageChange() throws IOException {
        when(servantRepository.findById(1)).thenReturn(Optional.of(servant));
        when(mediaService.saveImage(image, "servants")).thenReturn("new/image.png");
        when(servantRepository.save(any())).thenReturn(servant);

        servantService.update(1, dto);

        verify(mediaService).deleteImage("old/path.png");
    }


    @Test
    void update_shouldFail_whenNotFound() {
        when(servantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> servantService.update(1, dto));
    }

    // ---------------- DELETE ----------------

    @Test
    void delete_shouldSucceed() {
        when(servantRepository.findById(1)).thenReturn(Optional.of(servant));

        servantService.delete(1);

        verify(mediaService).deleteImage("old/path.png");
        verify(servantRepository).delete(servant);
    }

    @Test
    void delete_shouldFail_whenNotFound() {
        when(servantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> servantService.delete(1));
    }
}
