package com.devekoc.altaris.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "province")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Province extends EcclesiasticalUnit {

    @Column(name = "siege")
    @Size(min = 1, max = 50, message = "Le siège de la Province doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le siège de la Province ne doit pas être vide !")
    private String headquarter;

    @Column(name = "archeveque")
    @Size(min = 1, max = 50, message = "Le nom de l'Archevêque de la Province doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom de l'Archevêque de la Province ne doit pas être vide !")
    private String archbishop;


}
