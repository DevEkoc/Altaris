package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.assignments.AssignmentCreateDTO;
import com.devekoc.altaris.dto.assignments.AssignmentListDTO;
import com.devekoc.altaris.services.AssignmentService;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("assignments")
@RequiredArgsConstructor
@Tag(name = "Assignments", description = "Management of altar server assignments within ecclesiastical units."
)public class AssignmentController {
    private final AssignmentService assignmentService;

    @Operation(
            summary = "Create a new assignment",
            description = """
                    Creates a new assignment linking a servant to an office within an ecclesiastical unit.
                    Business rules enforced:
                    - The office must be active
                    - The servant must belong to the jurisdiction of the office
                    - A servant cannot hold more than one active assignment at the same ecclesiastical level
                    - A position cannot be duplicated within the same office
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Assignment successfully created"),
            @ApiResponse(responseCode = "400", description = "Business rule violation or invalid input data"),
            @ApiResponse(responseCode = "404", description = "Servant or Office not found")
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull AssignmentListDTO> create(@Valid @RequestBody AssignmentCreateDTO dto) {
        AssignmentListDTO created = assignmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List all assignments",
            description = "Retrieves all registered assignments, including inactive ones."
    )
    @ApiResponse(responseCode = "200", description = "List of assignments retrieved successfully")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<AssignmentListDTO>> listAll() {
        List<AssignmentListDTO> assignments = assignmentService.listAll();
        return ResponseEntity.ok(assignments);
    }

    @Operation(
            summary = "Get assignment by ID",
            description = "Retrieves detailed information about a specific assignment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assignment found"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull AssignmentListDTO> findById(@PathVariable int id) {
        AssignmentListDTO found = assignmentService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(
            summary = "Update an assignment",
            description = """
                    Updates an existing assignment.
                    The same business rules as creation apply, except that the current assignment is excluded
                    from uniqueness checks.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assignment successfully updated"),
            @ApiResponse(responseCode = "400", description = "Business rule violation or invalid input data"),
            @ApiResponse(responseCode = "404", description = "Assignment, Servant or Office not found")
    })
    @PutMapping(path = "id/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull AssignmentListDTO> update(@PathVariable int id, @Valid @RequestBody AssignmentCreateDTO dto) {
        AssignmentListDTO updated = assignmentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete an assignment",
            description = "Deletes an assignment permanently."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Assignment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
