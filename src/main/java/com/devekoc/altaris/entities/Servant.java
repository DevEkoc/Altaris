package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.Gender;
import com.devekoc.altaris.enumerations.ServantGrade;
import com.devekoc.altaris.validation.ValidCustomPhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "servant")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Servant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "matricule", unique = true)
    private String serialNumber;

    @Column(name = "nom")
    @Size(min = 1, max = 50, message = "Le nom du Servant doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Servant ne doit pas être vide !")
    private String name;

    @Column(name = "prenom")
    @Size(max = 50, message = "Le prénom du Servant doit contenir au max 50 caractères.")
    private String surname;

    @Column(name = "dateNaissance")
    @NotNull(message = "La date de naissance ne doit pas être vide !")
    private LocalDate birthDate;

    @Column(name = "sexe")
    @NotBlank(message = "Le sexe ne doit pas être vide !")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "dateEntree")
    private LocalDate entryDate;

    @Column
    @Enumerated(EnumType.STRING)
    private ServantGrade grade;

    @Column
    @ValidCustomPhoneNumber
    @NotBlank(message = "Le téléphone ne doit pas être vide !")
    private String phone;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idParoisse")
    private Parish parish;
}
