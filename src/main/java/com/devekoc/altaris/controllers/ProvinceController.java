package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ProvinceCreateDTO;
import com.devekoc.altaris.dto.ProvinceListDTO;
import com.devekoc.altaris.services.ProvinceService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<@NonNull ProvinceListDTO> create(@Valid @ModelAttribute ProvinceCreateDTO dto) throws IOException {
        ProvinceListDTO created = provinceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
