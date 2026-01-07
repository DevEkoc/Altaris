package com.devekoc.altaris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Summary information of an Office")
public record OfficeListDTO(
        @Schema(example = "10") Integer id,
        @Schema(example = "01-01-2026") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate creationDate,
        @Schema(example = "false")boolean active,
        @Schema(example = "Altar Servant's Office of the Ecclesiastical Province of Bertoua")String description
) {
}
