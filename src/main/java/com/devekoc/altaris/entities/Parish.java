package com.devekoc.altaris.entities;

import com.devekoc.altaris.enumerations.ParishType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "paroisse")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Parish extends EcclesiasticalUnit{

    @Column(name = "cure")
    @Size(min = 1, max = 50, message = "Le nom du Curé doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Curé ne doit pas être vide !")
    private String priest;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de la Paroisse ne doit pas être vide !")
    private ParishType type;

    @OneToMany(mappedBy = "parish")
    @JsonIgnore
    private List<Servant> servants;
}
