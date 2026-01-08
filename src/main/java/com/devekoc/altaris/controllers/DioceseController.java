package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.DioceseCreateDTO;
import com.devekoc.altaris.dto.DioceseListDTO;
import com.devekoc.altaris.services.DioceseService;
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
@RequestMapping("dioceses")
@RequiredArgsConstructor
@Tag(name = "Dioceses", description = "Management of Dioceses")
public class DioceseController {
    private final DioceseService dioceseService;

    @Operation(
            summary = "Create a new diocese",
            description = "Registers a new diocese within a specific province. Supports image upload and requires a diocese type (ARCHIDIOCESE or SUFFRAGANT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Diocese successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or image format"),
            @ApiResponse(responseCode = "404", description = "Parent Province, Chaplain, or Office ID not found"),
            @ApiResponse(responseCode = "409", description = "A diocese with this name already exists")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> create(@Valid @ModelAttribute DioceseCreateDTO dto) throws IOException {
        DioceseListDTO created = dioceseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search dioceses",
            description = "Retrieves all dioceses or filters them using a global search. " +
                    "The search 'q' matches against: diocese details (name, bishop, type, locality), " +
                    "the parent province name, chaplain details, and office description."
    )
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

    @Operation(summary = "Get diocese by ID", description = "Fetches detailed information about a diocese by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diocese found"),
            @ApiResponse(responseCode = "404", description = "Diocese not found")
    })
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> findById(@PathVariable int id) {
        DioceseListDTO found = dioceseService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update a diocese", description = "Updates an existing diocese's information, including its parent province or image.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull DioceseListDTO> update(@PathVariable int id, @Valid @ModelAttribute DioceseCreateDTO dto) throws IOException {
        DioceseListDTO updated = dioceseService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a diocese", description = "Permanently removes a diocese and its associated media from the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Diocese successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Diocese not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete a diocese containing Pastoral Zones")
    })
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        dioceseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
