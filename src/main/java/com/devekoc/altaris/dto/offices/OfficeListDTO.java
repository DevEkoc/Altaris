package com.devekoc.altaris.dto.offices;

import com.devekoc.altaris.dto.assignments.AssignmentListDTO;
import com.devekoc.altaris.enumerations.EcclesiasticalLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Summary information of an Office")
public record OfficeListDTO(
        @Schema(example = "10") Integer id,
        @Schema(example = "01-01-2026") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate creationDate,
        @Schema(example = "false")boolean active,
        @Schema(example = "Altar Servant's Office of the Ecclesiastical Province of Bertoua")String description,
        @Schema(example = "PARISH")EcclesiasticalLevel ecclesiasticalLevel,
        @Schema(example = "10")Integer unitId,
        @Schema(example = "Sainte Thérèse de Nkolfoulou")String unitName,
        @Schema(description = "List of the linked assignments") List<AssignmentListDTO> assignments
) {
}
