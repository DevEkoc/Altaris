package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.ChaplainCreateDTO;
import com.devekoc.altaris.dto.ChaplainListDTO;
import com.devekoc.altaris.services.ChaplainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("chaplains")
@RequiredArgsConstructor
@Tag(name = "Chaplains", description = "Management of Chaplains assigned to various ecclesiastical units")
public class ChaplainController {
    private final ChaplainService chaplainService;

    @Operation(
            summary = "Register a new chaplain",
            description = "Creates a record for a priest or religious officer acting as a chaplain for an ecclesiastical unit."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Chaplain successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., name or phone validation failed)")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ChaplainListDTO> create(@Valid @ModelAttribute ChaplainCreateDTO dto) throws IOException {
        ChaplainListDTO created = chaplainService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search chaplains",
            description = "Retrieves all chaplains or filters them by name, surname, or phone number."
    )
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ChaplainListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ChaplainListDTO> chaplains;
        if (q != null && !q.isEmpty()) {
            chaplains = chaplainService.find(q);
        } else {
            chaplains = chaplainService.listAll();
        }
        return ResponseEntity.ok(chaplains);
    }

    @Operation(summary = "Get chaplain by ID", description = "Fetches the full profile of a specific chaplain.")
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ChaplainListDTO> findById(@PathVariable int id) {
        ChaplainListDTO found = chaplainService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update chaplain info", description = "Updates personal and contact information for an existing chaplain.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ChaplainListDTO> update(@PathVariable int id, @Valid @ModelAttribute ChaplainCreateDTO dto) throws IOException {
        ChaplainListDTO updated = chaplainService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove a chaplain", description = "Permanently removes a chaplain from the system. **Warning**: May fail if assigned to active units.")
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        chaplainService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
