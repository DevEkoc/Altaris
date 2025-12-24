package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.DioceseCreateDTO;
import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.services.DioceseService;
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
@RequestMapping("dioceses")
@RequiredArgsConstructor
public class DioceseController {
    private final DioceseService dioceseService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> create(@Valid @ModelAttribute DioceseCreateDTO dto) throws IOException {
        DioceseListDTO created = dioceseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<DioceseListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<DioceseListDTO> dioceses;
        if (q != null && !q.isEmpty()) {
            dioceses = dioceseService.find(q);
        } else {
            dioceses = dioceseService.listAll();
        }
        return ResponseEntity.ok(dioceses);
    }

    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> findById(@PathVariable int id) {
        DioceseListDTO found = dioceseService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> update(@PathVariable int id, @Valid @ModelAttribute DioceseCreateDTO dto) throws IOException {
        DioceseListDTO updated = dioceseService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        dioceseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
