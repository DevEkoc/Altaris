package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.zones.ZoneCreateDTO;
import com.devekoc.altaris.dto.zones.ZoneDetailsDTO;
import com.devekoc.altaris.dto.zones.ZoneListDTO;
import com.devekoc.altaris.services.ZoneService;
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
@RequestMapping("zones")
@RequiredArgsConstructor
@Tag(name = "Zones", description = "Pastoral Zones Management (intermediate level between Diocese and Parish)")
public class ZoneController {
    private final ZoneService zoneService;

    @Operation(
            summary = "Create a new pastoral zone",
            description = "Registers a new zone within a diocese. Includes details about the Episcopal Vicar and supports image upload."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Zone successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or image format"),
            @ApiResponse(responseCode = "404", description = "Parent Diocese, Chaplain, or Office ID not found"),
            @ApiResponse(responseCode = "409", description = "A zone with this name already exists")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ZoneListDTO> create(@Valid @ModelAttribute ZoneCreateDTO dto) throws IOException {
        ZoneListDTO created = zoneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search zones",
            description = "Retrieves all zones or filters them using a global search. " +
                    "The search 'q' matches against: zone details (name, description, patron saint, locality, episcopal vicar), " +
                    "the parent diocese name, chaplain details (name, surname), and office description."
    )
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

    @Operation(summary = "Get zone by ID", description = "Fetches detailed information about a specific pastoral zone.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone found"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ZoneDetailsDTO> findById(@PathVariable int id) {
        ZoneDetailsDTO found = zoneService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update a zone", description = "Updates an existing zone's information, including the assigned Episcopal Vicar or parent diocese.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ZoneListDTO> update(@PathVariable int id, @Valid @ModelAttribute ZoneCreateDTO dto) throws IOException {
        ZoneListDTO updated = zoneService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a zone",
            description = "Permanently removes a zone. **Note:** Deletion will fail if the zone still contains registered parishes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Zone successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Zone not found"),
            @ApiResponse(responseCode = "409", description = "Conflict: Cannot delete a zone that contains parishes")
    })
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
