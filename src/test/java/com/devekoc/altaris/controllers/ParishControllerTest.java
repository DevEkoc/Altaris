package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ParishListDTO;
import com.devekoc.altaris.enumerations.ParishType;
import com.devekoc.altaris.services.ParishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParishController.class)
@AutoConfigureMockMvc(addFilters = false)
class ParishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParishService parishService;

    private ParishListDTO parishDTO() {
        return new ParishListDTO(
                1,
                "Paroisse Test",
                "Description",
                "Saint Patron",
                "image.jpg",
                "Yaoundé",
                null,
                null,
                "Curé Test",
                ParishType.PAROISSE,
                "Zone Centre"
        );
    }

    /* =========================
       CREATE
       ========================= */

    @Test
    void create_shouldReturn201() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        when(parishService.create(any())).thenReturn(parishDTO());

        mockMvc.perform(multipart("/parishes")
                        .file(image)
                        .param("name", "Paroisse Test")
                        .param("description", "Description")
                        .param("saintPatron", "Saint Patron")
                        .param("locality", "Yaoundé")
                        .param("priest", "Curé Test")
                        .param("type", "PAROISSE")
                        .param("zoneId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Paroisse Test"));
    }

    /* =========================
       LIST / SEARCH
       ========================= */

    @Test
    void listAll_shouldReturn200() throws Exception {
        when(parishService.listAll()).thenReturn(List.of(parishDTO()));

        mockMvc.perform(get("/parishes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void search_shouldReturn200() throws Exception {
        when(parishService.find("test")).thenReturn(List.of(parishDTO()));

        mockMvc.perform(get("/parishes").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    /* =========================
       FIND BY ID
       ========================= */

    @Test
    void findById_shouldReturn200_whenFound() throws Exception {
        when(parishService.findById(1)).thenReturn(parishDTO());

        mockMvc.perform(get("/parishes/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        when(parishService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/parishes/id/1"))
                .andExpect(status().isNotFound());
    }

    /* =========================
       UPDATE
       ========================= */

    @Test
    void update_shouldReturn200() throws Exception {
        when(parishService.update(eq(1), any())).thenReturn(parishDTO());

        mockMvc.perform(multipart("/parishes/id/1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .param("name", "Paroisse Test")
                        .param("description", "Description")
                        .param("saintPatron", "Saint Patron")
                        .param("locality", "Yaoundé")
                        .param("priest", "Curé Test")
                        .param("type", "PAROISSE")
                        .param("zoneId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paroisse Test"));
    }

    /* =========================
       DELETE
       ========================= */

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(parishService).delete(1);

        mockMvc.perform(delete("/parishes/id/1"))
                .andExpect(status().isNoContent());
    }
}
