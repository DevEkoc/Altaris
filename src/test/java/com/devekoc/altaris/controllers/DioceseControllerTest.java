package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.enumerations.DioceseType;
import com.devekoc.altaris.services.DioceseService;
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

@WebMvcTest(DioceseController.class)
@AutoConfigureMockMvc(addFilters = false)
class DioceseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DioceseService dioceseService;

    /* ======================================================
       CREATE
       ====================================================== */

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "diocese.png", "image/png", "fake".getBytes()
        );

        DioceseListDTO response = new DioceseListDTO(
                1,
                "Diocèse de Yaoundé",
                "Description",
                "Saint Paul",
                "diocese.png",
                "Yaoundé",
                null,
                null,
                "Mgr X",
                null,
                DioceseType.ARCHIDIOCESE,
                "Province Centre"
        );

        when(dioceseService.create(any())).thenReturn(response);

        mockMvc.perform(multipart("/dioceses")
                        .file(image)
                        .param("name", "Diocèse de Yaoundé")
                        .param("description", "Description")
                        .param("saintPatron", "Saint Paul")
                        .param("locality", "Yaoundé")
                        .param("bishop", "Mgr X")
                        .param("type", "ARCHIDIOCESE")
                        .param("provinceId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Diocèse de Yaoundé"))
                .andExpect(jsonPath("$.bishop").value("Mgr X"));
    }

    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        mockMvc.perform(multipart("/dioceses")
                        .param("description", "Desc")
                        .param("type", "ARCHDIOCESE")
                )
                .andExpect(status().isBadRequest());
    }

    /* ======================================================
       FIND / LIST
       ====================================================== */

    @Test
    void listAll_shouldReturn200() throws Exception {
        when(dioceseService.listAll()).thenReturn(List.of());

        mockMvc.perform(get("/dioceses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void search_shouldReturn200() throws Exception {
        when(dioceseService.find("yao")).thenReturn(List.of());

        mockMvc.perform(get("/dioceses").param("q", "yao"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturn200_whenFound() throws Exception {
        DioceseListDTO dto = new DioceseListDTO(
                1, "Diocèse", null, null, null,
                null, null, null,
                "Mgr X", null,
                DioceseType.SUFFRAGANT,
                "Province"
        );

        when(dioceseService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/dioceses/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    /* ======================================================
       UPDATE
       ====================================================== */

    @Test
    void update_shouldReturn200() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.png", "image/png", "fake".getBytes()
        );

        DioceseListDTO updated = new DioceseListDTO(
                1,
                "Diocèse modifié",
                "Desc",
                null,
                "new.png",
                "Yaoundé",
                null,
                null,
                "Mgr Y",
                null,
                DioceseType.SUFFRAGANT,
                "Province"
        );

        when(dioceseService.update(eq(1), any())).thenReturn(updated);

        mockMvc.perform(multipart("/dioceses/id/1")
                        .file(image)
                        .param("name", "Diocèse modifié")
                        .param("description", "Desc")
                        .param("bishop", "Mgr Y")
                        .param("type", "SUFFRAGANT")
                        .param("provinceId", "1")
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image").value("new.png"));
    }

    /* ======================================================
       DELETE
       ====================================================== */

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(dioceseService).delete(1);

        mockMvc.perform(delete("/dioceses/id/1"))
                .andExpect(status().isNoContent());
    }
}

