package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.LocalityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "localite")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomLocalite")
    @NotBlank(message = "Le nom de la localité ne doit pas être vide !")
    @Size(min = 1, max = 50, message = "Le nom de la localité doit contenir entre 1 et 50 caractères.")
    private String name;

    @Column(name = "typeLocalite")
    @Enumerated(EnumType.STRING)
    private LocalityType localityType;

    @ManyToOne
    @JoinColumn(name = "idUnite")
    private EcclesiasticalUnit ecclesiasticalUnit;
}
