package com.devekoc.altaris.dto;

import com.devekoc.altaris.enumerations.PriestlyRank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Schema(description = "Summary information of a Chaplain")
public record ChaplainListDTO(
        @Schema(example = "10") Integer id,
        @Schema(example = "Emmanuel") String name,
        @Schema(example = "Abena") String surname,
        @Schema(example = "EVEQUE", allowableValues = {"DIACRE", "PRETRE", "EVEQUE"})@Enumerated(EnumType.STRING) PriestlyRank priestlyRank,
        @Schema(example = "690123456") String phone
) {}
