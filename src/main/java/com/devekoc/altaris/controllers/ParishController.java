package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ParishCreateDTO;
import com.devekoc.altaris.dto.ParishListDTO;
import com.devekoc.altaris.services.ParishService;
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
@RequestMapping("parishes")
@RequiredArgsConstructor
public class ParishController {
    private final ParishService parishService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ParishListDTO> create(@Valid @ModelAttribute ParishCreateDTO dto) throws IOException {
        ParishListDTO created = parishService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ParishListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ParishListDTO> parishes;
        if (q != null && !q.isEmpty()) {
            parishes = parishService.find(q);
        } else {
            parishes = parishService.listAll();
        }
        return ResponseEntity.ok(parishes);
    }

    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ParishListDTO> findById(@PathVariable int id) {
        ParishListDTO found = parishService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ParishListDTO> update(@PathVariable int id, @Valid @ModelAttribute ParishCreateDTO dto) throws IOException {
        ParishListDTO updated = parishService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        parishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
