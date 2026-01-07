package com.devekoc.altaris.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response structure")
public record ErrorEntity(
        @Schema(example = "404", description = "HTTP Status code or internal error code")
        String code,
        @Schema(example = "Province with ID 5 not found", description = "Human-readable error message")
        String message
) {
}
