package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ServantListDTO;
import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.devekoc.altaris.services.ServantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServantController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServantService servantService;

    @Autowired
    private ObjectMapper objectMapper;

    private ServantListDTO servantDTO() {
        return new ServantListDTO(
                1,
                "SRV-001",
                "Jean",
                "Dupont",
                LocalDate.of(2005, 5, 10),
                Gender.MASCULIN,
                LocalDate.of(2020, 1, 1),
                ServantGrade.ACOLYTE,
                "699999999",
                "image/path.png",
                "Paroisse Test"
        );
    }

    /* ============================
       CREATE
       ============================ */

    @Test
    void create_shouldReturn201() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "image.png", "image/png", "fake".getBytes()
        );

        when(servantService.create(any())).thenReturn(servantDTO());

        mockMvc.perform(multipart("/servants")
                        .file(image)
                        .param("name", "Jean")
                        .param("surname", "Dupont")
                        .param("birthDate", "2005-05-10")
                        .param("gender", "MASCULIN")
                        .param("entryDate", "2020-01-01")
                        .param("grade", "ACOLYTE")
                        .param("phone", "699999999")
                        .param("parishId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jean"));
    }

    /* ============================
       LIST / SEARCH
       ============================ */

    @Test
    void listAll_shouldReturn200() throws Exception {
        when(servantService.listAll()).thenReturn(List.of(servantDTO()));

        mockMvc.perform(get("/servants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void search_shouldReturnFilteredList() throws Exception {
        when(servantService.find("jean")).thenReturn(List.of(servantDTO()));

        mockMvc.perform(get("/servants").param("q", "jean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jean"));
    }

    /* ============================
       FIND BY ID
       ============================ */

    @Test
    void findById_shouldReturn200_whenFound() throws Exception {
        when(servantService.findById(1)).thenReturn(servantDTO());

        mockMvc.perform(get("/servants/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        when(servantService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/servants/id/1"))
                .andExpect(status().isNotFound());
    }

    /* ============================
       UPDATE
       ============================ */

    @Test
    void update_shouldReturn200() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "image.png", "image/png", "new".getBytes()
        );

        when(servantService.update(eq(1), any())).thenReturn(servantDTO());

        mockMvc.perform(multipart("/servants/id/1")
                        .file(image)
                        .param("name", "Jean")
                        .param("surname", "Dupont")
                        .param("birthDate", "2005-05-10")
                        .param("gender", "MASCULIN")
                        .param("entryDate", "2020-01-01")
                        .param("grade", "ACOLYTE")
                        .param("phone", "699999999")
                        .param("parishId", "1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jean"));
    }

    /* ============================
       DELETE
       ============================ */

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/servants/id/1"))
                .andExpect(status().isNoContent());
    }
}
