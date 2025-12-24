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
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ProvinceListDTO> create(@Valid @ModelAttribute ProvinceCreateDTO dto) throws IOException {
        ProvinceListDTO created = provinceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ProvinceListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ProvinceListDTO> provinces;
        if (q != null && !q.isEmpty()) {
            provinces = provinceService.find(q);
        } else {
            provinces = provinceService.listAll();
        }
        return ResponseEntity.ok(provinces);
    }

    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ProvinceListDTO> findById(@PathVariable int id) {
        ProvinceListDTO found = provinceService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ProvinceListDTO> update(@PathVariable int id, @Valid @ModelAttribute ProvinceCreateDTO dto) throws IOException {
        ProvinceListDTO updated = provinceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        provinceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
