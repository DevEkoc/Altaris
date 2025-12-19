package com.devekoc.altaris.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "unite")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EcclesiasticalUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "nomUnite")
    @NotBlank(message = "Le nom de l'unité ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le nom doit contenir entre 1 et 50 caractères.")
    protected String name;

    @Column(name = "initiales")
    @NotBlank(message = "Les initiales ne doivent pas être vides.")
    @Size(min = 3, max = 5, message = "Les initiales doivent contenir entre 3 et 5 caractères.")
    protected String initials;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La description ne doit pas être vide !")
    protected String description;

    @Column
    protected String saintPatron;

    @Column
    protected String image;

    @OneToMany(mappedBy = "ecclesiasticalUnit")
    private List<Locality> localities;

    @ManyToOne
    @JoinColumn(name = "aumonier")
    private Chaplain chaplain;

    @OneToOne
    @JoinColumn(name = "bureau")
    private Office office;
}
