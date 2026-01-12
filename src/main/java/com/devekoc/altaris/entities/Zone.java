package com.devekoc.altaris.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Zone extends EcclesiasticalUnit{

    @Column
    @Size(min = 1, max = 50, message = "Le nom du Vicaire Episcopal doit contenir entre 1 et 50 caractères.")
    @NotBlank(message = "Le nom du Vicaire Episcopal ne doit pas être vide !")
    private String episcopalVicar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Diocese diocese;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Parish> parishList = new ArrayList<>();
}
