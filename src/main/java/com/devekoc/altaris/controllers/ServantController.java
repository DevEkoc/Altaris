package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ServantCreateDTO;
import com.devekoc.altaris.dto.ServantListDTO;
import com.devekoc.altaris.services.ServantService;
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
@RequestMapping("servants")
@RequiredArgsConstructor
public class ServantController {
    private final ServantService servantService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> create(@Valid @ModelAttribute ServantCreateDTO dto) throws IOException {
        ServantListDTO created = servantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ServantListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ServantListDTO> servantes;
        if (q != null && !q.isEmpty()) {
            servantes = servantService.find(q);
        } else {
            servantes = servantService.listAll();
        }
        return ResponseEntity.ok(servantes);
    }

    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> findById(@PathVariable int id) {
        ServantListDTO found = servantService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> update(@PathVariable int id, @Valid @ModelAttribute ServantCreateDTO dto) throws IOException {
        ServantListDTO updated = servantService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        servantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
