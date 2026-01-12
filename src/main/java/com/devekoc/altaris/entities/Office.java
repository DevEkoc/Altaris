package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.EcclesiasticalLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull(message = "La date de création ne doit pas être vide !")
    private LocalDate creationDate;

    @Column
    @Builder.Default
    private boolean active = true;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La description ne doit pas être vide !")
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le niveau ecclésiastique ne doit pas être vide !")
    EcclesiasticalLevel ecclesiasticalLevel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private EcclesiasticalUnit unit;

    public boolean canContain(Servant servant) {
        return switch (ecclesiasticalLevel) {
            case PARISH -> Objects.equals(unit.getId(), servant.getParish().getId());
            case ZONE -> Objects.equals(unit.getId(), servant.getParish().getZone().getId());
            case DIOCESE -> Objects.equals(unit.getId(), servant.getParish().getZone().getDiocese().getId());
            case PROVINCE -> Objects.equals(unit.getId(), servant.getParish().getZone().getDiocese().getProvince().getId());
        };
    }
}
