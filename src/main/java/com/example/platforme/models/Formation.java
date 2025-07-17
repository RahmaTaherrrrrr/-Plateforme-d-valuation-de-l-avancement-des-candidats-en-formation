package com.example.platforme.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "formations")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private Date dateDebut;
    private Date dateFin;

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Utilisateur formateur;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private List<Module> modules;

    // Constructeurs
    public Formation() {}

    public Formation(Long id, String titre, String description, Date dateDebut, Date dateFin, Utilisateur formateur, List<Module> modules) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.setFormateur(formateur); // appel du setter custom
        this.modules = modules;
    }

    // Getters & Setters
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Utilisateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Utilisateur formateur) {
        if (formateur != null && formateur.getRole() != Role.FORMATEUR) {
            throw new IllegalArgumentException("L'utilisateur assigné n'est pas un formateur.");
        }
        this.formateur = formateur;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
