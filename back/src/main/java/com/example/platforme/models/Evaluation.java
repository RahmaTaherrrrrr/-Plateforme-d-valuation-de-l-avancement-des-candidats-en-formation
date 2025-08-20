package com.example.platforme.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changé de Integer à Long

    private Date dateEvaluation;
    private String typeEvaluation;
    private int duree;

    @ManyToOne
    @JoinColumn(name = "module_id") // Ajouté pour spécifier la clé étrangère
    private Module module;

    // Constructeurs
    public Evaluation() {}

    public Evaluation(Long id, Date dateEvaluation, String typeEvaluation, int duree, Module module) {
        this.id = id;
        this.dateEvaluation = dateEvaluation;
        this.typeEvaluation = typeEvaluation;
        this.duree = duree;
        this.module = module;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public String getTypeEvaluation() {
        return typeEvaluation;
    }

    public void setTypeEvaluation(String typeEvaluation) {
        this.typeEvaluation = typeEvaluation;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}