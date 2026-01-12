package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.servants.ServantCreateDTO;
import com.devekoc.altaris.dto.servants.ServantListDTO;
import com.devekoc.altaris.services.ServantService;
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
@RequestMapping("servants")
@RequiredArgsConstructor
@Tag(name = "Altar Servers", description = "Altar servers management, including grading and parish assignment")
public class ServantController {
    private final ServantService servantService;

    @Operation(
            summary = "Register a new altar server",
            description = "Creates a server profile and automatically generates a unique serial number (format: SERYYYYXXX). Requires birth date and parish assignment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Servant successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or image format"),
            @ApiResponse(responseCode = "404", description = "Assigned Parish ID not found")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> create(@Valid @ModelAttribute ServantCreateDTO dto) throws IOException {
        ServantListDTO created = servantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Search altar servers",
            description = "Search servers by name, serial number, grade, phone, or their parish name."
    )
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

    @Operation(summary = "Get servant by ID", description = "Retrieves full profile of an altar server.")
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> findById(@PathVariable int id) {
        ServantListDTO found = servantService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update servant info", description = "Updates profile details, grade, or changes the server's assigned parish.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ServantListDTO> update(@PathVariable int id, @Valid @ModelAttribute ServantCreateDTO dto) throws IOException {
        ServantListDTO updated = servantService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove a servant", description = "Permanently deletes a servant's record and their associated profile picture.")
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        servantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
