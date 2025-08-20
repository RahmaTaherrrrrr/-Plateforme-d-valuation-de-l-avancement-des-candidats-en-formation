// Fichier : com/example/platforme/dtos/FormationDTO.java

package com.example.platforme.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List; // ** IMPORTANT : Importer java.util.List **

public class FormationDTO {

    private Long id;

    @NotBlank(message = "Le titre est requis")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @NotBlank(message = "La date de début est requise")
    private String dateDebut;

    @NotBlank(message = "La date de fin est requise")
    private String dateFin;

    @NotNull(message = "L'ID du formateur est requis")
    private Long formateurId;

    private String formateurNom; // Ce champ est pour l'affichage, pas la création

    // ======================= LA MODIFICATION EST ICI =======================
    // On ajoute la liste des IDs des modules pour correspondre au formulaire Angular.
    // C'est le champ le plus important qui manque.
    private List<Long> moduleIds;
    // =====================================================================

    // Constructeur par défaut
    public FormationDTO() {}

    // Getters et Setters pour TOUS les champs

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public Long getFormateurId() { return formateurId; }
    public void setFormateurId(Long formateurId) { this.formateurId = formateurId; }

    public String getFormateurNom() { return formateurNom; }
    public void setFormateurNom(String formateurNom) { this.formateurNom = formateurNom; }

    // Getter et Setter pour le nouveau champ
    public List<Long> getModuleIds() { return moduleIds; }
    public void setModuleIds(List<Long> moduleIds) { this.moduleIds = moduleIds; }
}
