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
@Table(name = "zone")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Zone extends EcclesiasticalUnit{

    @Column(name = "vicaireEpiscopal")
    @Size(min = 1, max = 50, message = "Le nom du Vicaire Episcopal doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Vicaire Episcopal ne doit pas être vide !")
    private String episcopalVicar;
}
