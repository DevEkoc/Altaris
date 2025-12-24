package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.*;
import com.devekoc.altaris.services.ProvinceService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProvinceController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProvinceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProvinceService provinceService;

    /* ===================== CREATE ===================== */

    @Test
    void create_shouldReturn201() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "img.png", "image/png", "fake".getBytes()
        );

        ProvinceListDTO response = new ProvinceListDTO(
                1, "Centre", "Desc", null,
                "img.png", "Yaoundé", "Mgr X",
                "Centre",
                null, null
        );

        when(provinceService.create(any()))
                .thenReturn(response);

        mockMvc.perform(multipart("/provinces")
                        .file(image)
                        .param("name", "Centre")
                        .param("description", "Desc")
                        .param("headquarter", "Yaoundé")
                        .param("archbishop", "Mgr X")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Centre"));
    }

    /* ===================== LIST ===================== */

    @Test
    void listAll_shouldReturn200() throws Exception {
        when(provinceService.listAll())
                .thenReturn(List.of(
                        new ProvinceListDTO(1, "Centre", "Desc", null,
                                null, "Yaoundé", "Mgr X", null, null, null)
                ));

        mockMvc.perform(get("/provinces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Centre"));
    }

    /* ===================== FIND BY ID ===================== */

    @Test
    void findById_shouldReturn200() throws Exception {
        when(provinceService.findById(1))
                .thenReturn(new ProvinceListDTO(
                        1, "Centre", "Desc", null,
                        null, "Yaoundé", "Mgr X", null, null, null
                ));

        mockMvc.perform(get("/provinces/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Centre"));
    }

    @Test
    void findById_shouldReturn404() throws Exception {
        when(provinceService.findById(99)).thenReturn(null);

        mockMvc.perform(get("/provinces/id/99"))
                .andExpect(status().isNotFound());
    }

    /* ===================== SEARCH ===================== */

    @Test
    void search_shouldReturnList() throws Exception {
        when(provinceService.find("bam"))
                .thenReturn(List.of(
                        new ProvinceListDTO(1, "Bamenda", "Desc", null,
                                null, "Bamenda", "Mgr Y", null, null, null)
                ));

        mockMvc.perform(get("/provinces").param("q", "bam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bamenda"));
    }

    /* ===================== UPDATE ===================== */

    @Test
    void update_shouldReturn200() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.png", "image/png", "fake".getBytes()
        );

        ProvinceListDTO updated = new ProvinceListDTO(
                1, "Centre", "Desc", null,
                "new.png", "Yaoundé", "Mgr X",
                null, null, null
        );

        when(provinceService.update(eq(1), any()))
                .thenReturn(updated);

        mockMvc.perform(multipart("/provinces/id/1")
                        .file(image)
                        .param("name", "Centre")
                        .param("description", "Desc")
                        .param("headquarter", "Yaoundé")
                        .param("archbishop", "Mgr X")
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image").value("new.png"));
    }

    /* ===================== DELETE ===================== */

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/provinces/id/1"))
                .andExpect(status().isNoContent());
    }
}
