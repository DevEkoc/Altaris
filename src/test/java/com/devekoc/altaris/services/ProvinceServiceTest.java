package com.devekoc.altaris.services;

import com.devekoc.altaris.dto.ProvinceCreateDTO;
import com.devekoc.altaris.dto.ProvinceListDTO;
import com.devekoc.altaris.entities.Chaplain;
import com.devekoc.altaris.entities.Office;
import com.devekoc.altaris.entities.Province;
import com.devekoc.altaris.repositories.ChaplainRepository;
import com.devekoc.altaris.repositories.OfficeRepository;
import com.devekoc.altaris.repositories.ProvinceRepository;
import com.devekoc.altaris.medias.MediaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvinceServiceTest {

    @InjectMocks
    private ProvinceService provinceService;

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private ChaplainRepository chaplainRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private MediaService mediaService;

    private Chaplain chaplain;
    private Office office;
    private Province province;
    private MultipartFile image;

    @BeforeEach
    void setup() {
        chaplain = new Chaplain(
                1,
                "Doe",
                "John",
                null,
                "699000000"
        );

        office = new Office(
                1,
                LocalDate.now(),
                true,
                "Bureau central"
        );

        province = new Province();
        province.setId(1);
        province.setName("Province A");
        province.setDescription("Description");
        province.setHeadquarter("Yaoundé");
        province.setArchbishop("Mgr Test");
        province.setImage("old.png");
        province.setChaplain(chaplain);
        province.setOffice(office);

        image = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                "img".getBytes()
        );
    }

    @Test
    void create_shouldSaveProvince() throws IOException {
        ProvinceCreateDTO dto = new ProvinceCreateDTO(
                "Province A",
                "Description",
                "Saint",
                image,
                "Centre",
                1,
                1,
                "Yaoundé",
                "Mgr Test"

                );

        when(provinceRepository.existsByName(dto.getName())).thenReturn(false);
        when(chaplainRepository.findById(1)).thenReturn(Optional.of(chaplain));
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));
        when(mediaService.saveImage(image, "provinces")).thenReturn("image.png");
        when(provinceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ProvinceListDTO result = provinceService.create(dto);

        assertThat(result.name()).isEqualTo("Province A");
        assertThat(result.headquarter()).isEqualTo("Yaoundé");
        assertThat(result.chaplain()).isNotNull();

        verify(mediaService).saveImage(image, "provinces");
        verify(provinceRepository).save(any(Province.class));
    }

    @Test
    void create_shouldFail_whenNameExists() {
        ProvinceCreateDTO dto = mock(ProvinceCreateDTO.class);

        when(dto.getName()).thenReturn("Province A");
        when(provinceRepository.existsByName("Province A")).thenReturn(true);

        assertThatThrownBy(() -> provinceService.create(dto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void create_shouldFail_whenChaplainNotFound() {
        ProvinceCreateDTO dto = mock(ProvinceCreateDTO.class);

        when(dto.getName()).thenReturn("Province A");
        when(dto.getChaplainId()).thenReturn(1);
        when(provinceRepository.existsByName(any())).thenReturn(false);
        when(chaplainRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> provinceService.create(dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findById_shouldReturnProvince() {
        when(provinceRepository.findById(1)).thenReturn(Optional.of(province));

        ProvinceListDTO dto = provinceService.findById(1);

        assertThat(dto.id()).isEqualTo(1);
    }

    @Test
    void findById_shouldFail_whenNotFound() {
        when(provinceRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> provinceService.findById(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void listAll_shouldReturnAll() {
        when(provinceRepository.findAll()).thenReturn(List.of(province));

        List<ProvinceListDTO> result = provinceService.listAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void find_shouldUseSpecification() {
        when(provinceRepository.findAll(Mockito.<Specification<@NonNull Province>>any()))
                .thenReturn(List.of(province));

        List<ProvinceListDTO> result = provinceService.find("test");

        assertThat(result).hasSize(1);
    }

    @Test
    void update_shouldReplaceImage() throws IOException {
        ProvinceCreateDTO dto = mock(ProvinceCreateDTO.class);

        when(dto.getName()).thenReturn("Province A");
        when(dto.getImage()).thenReturn(image);
        when(dto.getChaplainId()).thenReturn(chaplain.getId());
        lenient().when(dto.getOfficeId()).thenReturn(office.getId());

        when(provinceRepository.findById(1))
                .thenReturn(Optional.of(province));

        when(mediaService.saveImage(image, "provinces"))
                .thenReturn("new.png");

        when(provinceRepository.save(any(Province.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        provinceService.update(1, dto);

        verify(mediaService).deleteImage("old.png");
    }



    @Test
    void update_shouldKeepOldImage() throws IOException {
        ProvinceCreateDTO dto = mock(ProvinceCreateDTO.class);

        when(dto.getName()).thenReturn("Province A");
        when(dto.getImage()).thenReturn(null);
        when(dto.getChaplainId()).thenReturn(chaplain.getId());
        lenient().when(dto.getOfficeId()).thenReturn(office.getId());

        when(provinceRepository.findById(1))
                .thenReturn(Optional.of(province));

        when(provinceRepository.save(any(Province.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        provinceService.update(1, dto);

        verify(mediaService, never()).deleteImage(any());
    }



    @Test
    void delete_shouldRemoveProvince() {
        when(provinceRepository.findById(1)).thenReturn(Optional.of(province));

        provinceService.delete(1);

        verify(provinceRepository).delete(province);
    }


}