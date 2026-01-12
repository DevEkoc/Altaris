package com.devekoc.altaris.controllers;

import com.devekoc.altaris.dto.provinces.ProvinceCreateDTO;
import com.devekoc.altaris.dto.provinces.ProvinceDetailsDTO;
import com.devekoc.altaris.dto.provinces.ProvinceListDTO;
import com.devekoc.altaris.services.ProvinceService;
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

@Tag(name = "Provinces", description = "Ecclesiastical Provinces Management")
@RestController
@RequestMapping("provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @Operation(
            summary = "Create a new province",
            description = "Registers a new ecclesiastical province and associates it with its Chaplain and Office. Supports image upload (JPG, PNG, WEBP)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Province created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or invalid image format"),
            @ApiResponse(responseCode = "404", description = "Chaplain or Office ID not found"),
            @ApiResponse(responseCode = "409", description = "A province with this name already exists")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ProvinceListDTO> create(@Valid @ModelAttribute ProvinceCreateDTO dto) throws IOException {
        ProvinceListDTO created = provinceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "List or search provinces",
            description = "Retrieves all provinces or filters them using a global search pattern. " +
                    "The search 'q' matches against province details (name, description, patron saint, locality, headquarters, archbishop), " +
                    "chaplain details (name, surname), and office descriptions."
    )
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull List<ProvinceListDTO>> findOrListAll(@RequestParam(required = false) String q) {
        List<ProvinceListDTO> provinces;
        if (q != null && !q.isEmpty()) {
            provinces = provinceService.find(q);
        } else {
            provinces = provinceService.listAll();
        }
        return ResponseEntity.ok(provinces);
    }

    @Operation(summary = "Get province by ID", description = "Fetches a single province's details by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Province found"),
            @ApiResponse(responseCode = "404", description = "Province not found")
    })
    @GetMapping(path = "id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull ProvinceDetailsDTO> findById(@PathVariable int id) {
        ProvinceDetailsDTO found = provinceService.findById(id);
        return (found == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(found);
    }

    @Operation(summary = "Update a province", description = "Updates an existing province. Allows replacing the image and other metadata.")
    @PutMapping(path = "id/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ProvinceListDTO> update(@PathVariable int id, @Valid @ModelAttribute ProvinceCreateDTO dto) throws IOException {
        ProvinceListDTO updated = provinceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a province", description = "Removes a province and its associated image from the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Province deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Province not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete a province containing Dioceses")
    })
    @DeleteMapping(path = "id/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable int id) {
        provinceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
