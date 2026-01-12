package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.parishes.ParishCreateDTO;
import com.devekoc.altaris.dto.parishes.ParishDetailsDTO;
import com.devekoc.altaris.dto.parishes.ParishListDTO;
import com.devekoc.altaris.services.ParishService;
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
@RequestMapping("parishes")
@RequiredArgsConstructor
@Tag(name = "Parishes", description = "Parishes management")
public class ParishController {
    private final ParishService parishService;

    @Operation(
            summary = "Create a new parish",
            description = "Registers a new parish within a specific pastoral zone. Requires a parish priest's name and a type (e.g., PAROISSE or QUASI_PAROISSE)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Parish successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or image format"),
            @ApiResponse(responseCode = "404", description = "Parent Zone, Chaplain, or Office ID not found"),
            @ApiResponse(responseCode = "409", description = "A parish with this name already exists")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ParishListDTO> create(@Valid @ModelAttribute ParishCreateDTO dto) throws IOException {
        ParishListDTO created = parishService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search parishes",
            description = "Retrieves all parishes or filters them using a global search. "
                    +"The search 'q' matches against: parish details (name, description, patron saint, locality, priest, type), " +
                    "the parent zone name, chaplain details (name, surname), and office description."
    )
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

    @Operation(summary = "Get parish by ID", description = "Fetches detailed information about a specific parish.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parish found"),
            @ApiResponse(responseCode = "404", description = "Parish not found")
    })
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ParishDetailsDTO> findById(@PathVariable int id) {
        ParishDetailsDTO found = parishService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update a parish", description = "Updates an existing parish's information, priest, or parent zone.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ParishListDTO> update(@PathVariable int id, @Valid @ModelAttribute ParishCreateDTO dto) throws IOException {
        ParishListDTO updated = parishService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a parish",
            description = "Permanently removes a parish. **Note:** Deletion will fail if the parish has registered altar servers."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Parish successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Parish not found"),
            @ApiResponse(responseCode = "409", description = "Conflict: Cannot delete a parish that contains altar servers")
    })
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        parishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
