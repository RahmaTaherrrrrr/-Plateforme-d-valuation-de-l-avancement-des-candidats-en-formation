package com.example.platforme.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la gestion des modules
 * Correction : Ajout des validations et du champ formationTitre
 */
public class ModuleDTO {

    private Long id;

    @NotBlank(message = "Le titre est requis")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "La durée est requise")
    @Positive(message = "La durée doit être positive")
    private Integer duree; // Durée en heures

    @NotNull(message = "L'ID de la formation est requis")
    private Long formationId;

    // Champ pour l'affichage, pas pour la création/modification
    private String formationTitre;

    // Constructeur par défaut
    public ModuleDTO() {}

    // Constructeur avec paramètres
    public ModuleDTO(Long id, String titre, String description, Integer duree, Long formationId, String formationTitre) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.formationId = formationId;
        this.formationTitre = formationTitre;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public String getFormationTitre() {
        return formationTitre;
    }

    public void setFormationTitre(String formationTitre) {
        this.formationTitre = formationTitre;
    }

    @Override
    public String toString() {
        return "ModuleDTO{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", duree=" + duree +
                ", formationId=" + formationId +
                ", formationTitre='" + formationTitre + '\'' +
                '}';
    }
}

