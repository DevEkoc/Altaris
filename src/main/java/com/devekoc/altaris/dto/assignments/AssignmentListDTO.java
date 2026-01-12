package com.devekoc.altaris.dto.assignments;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detailed view of an Altar Server assignment, including servant and office profiles")
public record AssignmentListDTO(
        @Schema(example = "101")
        Integer id,

        @Schema(example = "Président de Bureau")
        String position,

        @Schema(example = "[\"Coordonner les activités\", \"Représenter le groupe\"]")
        List<String> missions,

        @Schema(description = "ID the assigned Altar Server")
        Integer servantId,

        @Schema(description = "Name the assigned Altar Server")
        String servantName
) {
}
