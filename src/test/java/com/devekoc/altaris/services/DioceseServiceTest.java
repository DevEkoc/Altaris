package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.DioceseCreateDTO;
import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Diocese;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.enumerations.DioceseType;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.DioceseRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.medias.MediaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DioceseServiceTest {

    @Mock
    private DioceseRepository dioceseRepository;
    @Mock
    private ProvinceService provinceService;
    @Mock
    private ChaplainRepository chaplainRepository;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private MediaService mediaService;

    @InjectMocks
    private DioceseService dioceseService;

    private DioceseCreateDTO dto;
    private Diocese diocese;
    private Province province;
    private Chaplain chaplain;
    private Office office;

    @BeforeEach
    void setUp() {
        province = new Province();
        province.setId(1);
        province.setName("Centre");

        chaplain = new Chaplain();
        chaplain.setId(1);
        chaplainRepository.save(chaplain);

        office = new Office();
        office.setId(1);

        diocese = new Diocese();
        diocese.setId(1);
        diocese.setName("Yaoundé");
        diocese.setProvince(province);
        diocese.setImage("old.png");
        diocese.setType(DioceseType.ARCHIDIOCESE);

        dto = new DioceseCreateDTO(
                "Yaoundé",
                "Description",
                "Saint Patron",
                null,
                "Yaoundé",
                1,
                1,
                "Mgr Test",
                null,
                DioceseType.ARCHIDIOCESE,
                1
        );
    }

    /* =======================
       CREATE
       ======================= */

    @Test
    void create_shouldCreateDioceseSuccessfully() throws Exception {
        when(dioceseRepository.existsByName("Yaoundé")).thenReturn(false);
        when(provinceService.findByIdOrThrow(1)).thenReturn(province);
        when(chaplainRepository.findById(1)).thenReturn(Optional.of(chaplain));
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));
        when(dioceseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        DioceseListDTO result = dioceseService.create(dto);

        assertNotNull(result);
        assertEquals("Yaoundé", result.name());
        assertEquals("Centre", result.provinceName());
        verify(dioceseRepository).save(any(Diocese.class));
    }

    @Test
    void create_shouldFail_whenNameAlreadyExists() {
        when(dioceseRepository.existsByName("Yaoundé")).thenReturn(true);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> dioceseService.create(dto)
        );
    }

    @Test
    void create_shouldFail_whenProvinceNotFound() {
        when(dioceseRepository.existsByName(any())).thenReturn(false);
        lenient().when(provinceService.findByIdOrThrow(1))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.create(dto)
        );
    }

    @Test
    void create_shouldFail_whenChaplainNotFound() {
        when(dioceseRepository.existsByName(any())).thenReturn(false);
        lenient().when(provinceService.findByIdOrThrow(1)).thenReturn(province);
        when(chaplainRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.create(dto)
        );
    }

    @Test
    void create_shouldFail_whenOfficeNotFound() {
        when(dioceseRepository.existsByName(any())).thenReturn(false);
        lenient().when(provinceService.findByIdOrThrow(1)).thenReturn(province);
        when(chaplainRepository.findById(1)).thenReturn(Optional.of(chaplain));
        when(officeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.create(dto)
        );
    }

    /* =======================
       FIND BY ID
       ======================= */

    @Test
    void findById_shouldReturnDTO() {
        when(dioceseRepository.findById(1)).thenReturn(Optional.of(diocese));

        DioceseListDTO result = dioceseService.findById(1);

        assertEquals("Yaoundé", result.name());
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(dioceseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.findById(1)
        );
    }

    /* =======================
       LIST ALL
       ======================= */

    @Test
    void listAll_shouldReturnAll() {
        when(dioceseRepository.findAll()).thenReturn(List.of(diocese));

        List<DioceseListDTO> result = dioceseService.listAll();

        assertEquals(1, result.size());
    }

    @Test
    void listAll_shouldReturnEmptyList() {
        when(dioceseRepository.findAll()).thenReturn(List.of());

        List<DioceseListDTO> result = dioceseService.listAll();

        assertTrue(result.isEmpty());
    }

    /* =======================
       UPDATE
       ======================= */

    @Test
    void update_shouldUpdateDioceseSuccessfully() throws Exception {
        when(dioceseRepository.findById(1)).thenReturn(Optional.of(diocese));
        lenient().when(dioceseRepository.existsByName("Yaoundé")).thenReturn(false);
        lenient().when(provinceService.findByIdOrThrow(1)).thenReturn(province);
        when(chaplainRepository.findById(1)).thenReturn(Optional.of(chaplain));
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));
        when(dioceseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        DioceseListDTO result = dioceseService.update(1, dto);

        assertEquals("Yaoundé", result.name());
        verify(dioceseRepository).save(any(Diocese.class));
    }

    @Test
    void update_shouldFail_whenDioceseNotFound() {
        when(dioceseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.update(1, dto)
        );
    }

    @Test
    void update_shouldFail_whenNewNameAlreadyExists() {
        DioceseCreateDTO dtoWithNewName = new DioceseCreateDTO(
                "Douala",
                dto.getDescription(),
                dto.getSaintPatron(),
                dto.getImage(),
                dto.getLocality(),
                dto.getChaplainId(),
                dto.getOfficeId(),
                dto.getBishop(),
                dto.getRetiredBishop(),
                dto.getType(),
                dto.getProvinceId()
        );
        when(dioceseRepository.findById(1)).thenReturn(Optional.of(diocese));
        when(dioceseRepository.existsByName("Douala")).thenReturn(true);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> dioceseService.update(1, dtoWithNewName)
        );
    }

    /* =======================
       DELETE
       ======================= */

    @Test
    void delete_shouldDeleteSuccessfully() {
        when(dioceseRepository.findById(1)).thenReturn(Optional.of(diocese));

        dioceseService.delete(1);

        verify(dioceseRepository).delete(diocese);
    }

    @Test
    void delete_shouldFail_whenNotFound() {
        when(dioceseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> dioceseService.delete(1)
        );
    }
}
