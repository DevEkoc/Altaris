package com.devekoc.altaris.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "affectation")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "poste")
    @NotBlank(message = "Le poste ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le poste doit contenir entre 1 et 50 caractères.")
    private String position;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La description ne doit pas être vide !")
    private String missions;

    @ManyToOne
    @NotNull(message = "Le servant ne doit pas être vide !")
    private Servant servant;

    @ManyToOne
    @NotNull(message = "Le bureau ne doit pas être vide !")
    private Office office;
}
