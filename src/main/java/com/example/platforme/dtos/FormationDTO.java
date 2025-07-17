package com.example.platforme.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FormationDTO {

    private Long id;
    @NotBlank(message = "Le titre est requis")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private String dateDebut;
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private String dateFin;
    private Long formateurId;
    private String formateurNom;

    public FormationDTO() {}

    // Getters et setters
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
}