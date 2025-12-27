package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ZoneListDTO;
import com.devekoc.altaris.services.ZoneService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZoneController.class)
@AutoConfigureMockMvc(addFilters = false)
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ZoneService zoneService;

    private ZoneListDTO sampleZone() {
        return new ZoneListDTO(
                1,
                "Zone Centre",
                "Description",
                "Saint Patron",
                "image.jpg",
                "Yaoundé",
                null,
                null,
                "Vicaire Test",
                "Diocèse Centre"
        );
    }

    /* ============================
       POST /zones
       ============================ */

    @Test
    void create_shouldReturnCreatedZone() throws Exception {
        ZoneListDTO zone = sampleZone();

        Mockito.when(zoneService.create(Mockito.any()))
                .thenReturn(zone);

        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "img".getBytes()
        );

        mockMvc.perform(multipart("/zones")
                        .file(image)
                        .param("name", "Zone Centre")
                        .param("description", "Description")
                        .param("saintPatron", "Saint Patron")
                        .param("locality", "Yaoundé")
                        .param("chaplainId", "1")
                        .param("officeId", "1")
                        .param("episcopalVicar", "Vicaire Test")
                        .param("dioceseId", "1")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Zone Centre"));
    }

    /* ============================
       GET /zones
       ============================ */

    @Test
    void listAll_shouldReturnZones() throws Exception {
        Mockito.when(zoneService.listAll())
                .thenReturn(List.of(sampleZone()));

        mockMvc.perform(get("/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void search_shouldReturnZones() throws Exception {
        Mockito.when(zoneService.find("centre"))
                .thenReturn(List.of(sampleZone()));

        mockMvc.perform(get("/zones")
                        .param("q", "centre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Zone Centre"));
    }

    /* ============================
       GET /zones/id/{id}
       ============================ */

    @Test
    void findById_shouldReturnZone_whenFound() throws Exception {
        Mockito.when(zoneService.findById(1))
                .thenReturn(sampleZone());

        mockMvc.perform(get("/zones/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(zoneService.findById(99))
                .thenReturn(null);

        mockMvc.perform(get("/zones/id/99"))
                .andExpect(status().isNotFound());
    }

    /* ============================
       PUT /zones/id/{id}
       ============================ */

    @Test
    void update_shouldReturnUpdatedZone() throws Exception {
        ZoneListDTO zone = sampleZone();

        Mockito.when(zoneService.update(Mockito.eq(1), Mockito.any()))
                .thenReturn(zone);

        MockMultipartFile image = new MockMultipartFile(
                "image", "update.jpg", MediaType.IMAGE_JPEG_VALUE, "img".getBytes()
        );

        mockMvc.perform(multipart("/zones/id/1")
                        .file(image)
                        .param("name", "Zone Centre")
                        .param("description", "Description")
                        .param("saintPatron", "Saint Patron")
                        .param("locality", "Yaoundé")
                        .param("chaplainId", "1")
                        .param("officeId", "1")
                        .param("episcopalVicar", "Vicaire Test")
                        .param("dioceseId", "1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zone Centre"));
    }

    /* ============================
       DELETE /zones/id/{id}
       ============================ */

    @Test
    void delete_shouldReturn204() throws Exception {
        Mockito.doNothing().when(zoneService).delete(1);

        mockMvc.perform(delete("/zones/id/1"))
                .andExpect(status().isNoContent());
    }
}
