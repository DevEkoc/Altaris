package com.devekoc.altaris.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "unite")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public abstract class EcclesiasticalUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "nomUnite")
    @NotBlank(message = "Le nom de l'unité ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le nom doit contenir entre 1 et 50 caractères.")
    protected String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La description ne doit pas être vide !")
    protected String description;

    @Column
    protected String saintPatron;

    @Column
    protected String image;

    @Column
    private String localite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aumonier")
    private Chaplain chaplain;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bureau")
    private Office office;
}
