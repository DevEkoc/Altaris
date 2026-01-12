package com.devekoc.altaris.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public abstract class EcclesiasticalUnit {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column
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
    private String locality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Chaplain chaplain;

//    @OneToOne(mappedBy = "unit")
//    private Office office;
}
