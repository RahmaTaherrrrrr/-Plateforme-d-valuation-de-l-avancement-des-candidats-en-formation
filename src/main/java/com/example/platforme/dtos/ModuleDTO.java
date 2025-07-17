package com.example.platforme.dtos;

public class ModuleDTO {

    private Long id;
    private String titre;
    private String description;
    private int duree;
    private Long formationId;

    public ModuleDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public Long getFormationId() { return formationId; }
    public void setFormationId(Long formationId) { this.formationId = formationId; }
}
