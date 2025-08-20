// Fichier : com/example/platforme/models/Formation.java

package com.example.platforme.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "formations")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    // ======================= LA CORRECTION EST ICI =======================

    // On dit à JPA : Le champ Java "dateDebut" correspond à la colonne "date_debut"
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_debut")
    private Date dateDebut;

    // On dit à JPA : Le champ Java "dateFin" correspond à la colonne "date_fin"
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_fin")
    private Date dateFin;

    // On dit à JPA : La relation "formateur" se fait via la colonne "formateur_id"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "formateur_id")
    private Utilisateur formateur;

    @JsonIgnore
    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Module> modules;

    // Constructeurs, Getters et Setters... (le reste de votre fichier est bon)
    public Formation() {}

    public Formation(Long id, String titre, String description, Date dateDebut, Date dateFin, Utilisateur formateur, List<Module> modules) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.setFormateur(formateur);
        this.modules = modules;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public Utilisateur getFormateur() { return formateur; }
    public void setFormateur(Utilisateur formateur) { this.formateur = formateur; }
    public List<Module> getModules() { return modules; }
    public void setModules(List<Module> modules) { this.modules = modules; }
}
