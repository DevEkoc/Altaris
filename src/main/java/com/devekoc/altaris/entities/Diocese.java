package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.DioceseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diocese")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Diocese extends EcclesiasticalUnit{

    @Column(name = "eveque")
    @Size(min = 1, max = 50, message = "Le nom de l'Évêque doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom de l'Évêque ne doit pas être vide !")
    private String bishop;

    @Column(name = "evequeEmerite")
    @Size(max = 50, message = "Le nom de l'Évêque émérite doit contenir au max 50 caractères.")
    private String retiredBishop;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type du diocèse ne doit pas être vide !")
    private DioceseType type;
}
