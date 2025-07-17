package com.example.platforme.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private int duree;

    @ManyToOne
    private Formation formation;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations;

    // 🔽 Constructeur vide
    public Module() {
    }

    // 🔽 Constructeur complet
    public Module(Long id, String titre, String description, int duree, Formation formation, List<Evaluation> evaluations) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.formation = formation;
        this.evaluations = evaluations;
    }

    // 🔽 Getters
    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public int getDuree() {
        return duree;
    }

    public Formation getFormation() {
        return formation;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    // 🔽 Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
