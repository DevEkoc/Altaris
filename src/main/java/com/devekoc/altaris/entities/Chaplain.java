package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.PriestlyRank;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aumonier")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Chaplain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomAumonier")
    @NotBlank(message = "Le nom de l'Aumônier ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le nom de l'Aumônier doit contenir entre 1 et 50 caractères.")
    private String name;

    @Column(name = "prenomAumonier")
    @Size(max = 50, message = "Le prénom nom de l'Aumônier doit contenir au max 50 caractères.")
    private String surname;

    @Column(name = "niveauSacerdotal")
    @NotNull(message = "Le niveau sacerdotal de l'Aumônier ne doit pas être vide !")
    private PriestlyRank priestlyRank;

    @Column
    @ValidCustomPhoneNumber
    @NotBlank(message = "Le téléphone ne doit pas être vide !")
    private String phone;
}
