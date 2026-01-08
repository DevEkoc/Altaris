package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.OfficeCreateDTO;
import com.devekoc.altaris.dto.OfficeListDTO;
import com.devekoc.altaris.services.OfficeService;
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
@RequestMapping("offices")
@RequiredArgsConstructor
@Tag(name = "Offices", description = "Management of administrative Offices (Bureaux) for the ecclesiastical units.")
public class OfficeController {
    private final OfficeService officeService;

    @Operation(
            summary = "Register a new office",
            description = "Creates a new administrative office. This office can later be linked to specific assignments for altar servers."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Office successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (check description or specific constraints)")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull OfficeListDTO> create(@Valid @ModelAttribute OfficeCreateDTO dto) throws IOException {
        OfficeListDTO created = officeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search offices",
            description = "Retrieves all registered offices. Global search (q) typically matches the office description or status (active or not)."
    )
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<OfficeListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<OfficeListDTO> offices;
        if (q != null && !q.isEmpty()) {
            offices = officeService.find(q);
        } else {
            offices = officeService.listAll();
        }
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Get office by ID", description = "Retrieves detailed information about a specific administrative office.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Office found"),
            @ApiResponse(responseCode = "404", description = "Office not found")
    })@GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull OfficeListDTO> findById(@PathVariable int id) {
        OfficeListDTO found = officeService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update office info", description = "Updates the description or other details of an existing office.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull OfficeListDTO> update(@PathVariable int id, @Valid @ModelAttribute OfficeCreateDTO dto) throws IOException {
        OfficeListDTO updated = officeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Remove an office",
            description = "Permanently deletes an office. **Constraint**: Deletion will be rejected (409 Conflict) if any Altar Servers are currently assigned to this office."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Office successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Office not found"),
            @ApiResponse(responseCode = "409", description = "Conflict: Cannot delete office with active servant assignments")
    })    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        officeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
