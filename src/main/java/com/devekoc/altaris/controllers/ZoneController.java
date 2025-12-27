package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ZoneCreateDTO;
import com.devekoc.altaris.dto.ZoneListDTO;
import com.devekoc.altaris.services.ZoneService;
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
@RequestMapping("zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ZoneListDTO> create(@Valid @ModelAttribute ZoneCreateDTO dto) throws IOException {
        ZoneListDTO created = zoneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ZoneListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ZoneListDTO> zones;
        if (q != null && !q.isEmpty()) {
            zones = zoneService.find(q);
        } else {
            zones = zoneService.listAll();
        }
        return ResponseEntity.ok(zones);
    }

    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ZoneListDTO> findById(@PathVariable int id) {
        ZoneListDTO found = zoneService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ZoneListDTO> update(@PathVariable int id, @Valid @ModelAttribute ZoneCreateDTO dto) throws IOException {
        ZoneListDTO updated = zoneService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
