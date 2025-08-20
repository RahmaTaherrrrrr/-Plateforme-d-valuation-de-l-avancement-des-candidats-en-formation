package com.example.platforme.dtos;

import java.util.Date;

public class EvaluationDTO {
    private Integer id;
    private Date dateEvaluation;
    private String typeEvaluation;
    private int duree;
    private Long moduleId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Date getDateEvaluation() { return dateEvaluation; }
    public void setDateEvaluation(Date dateEvaluation) { this.dateEvaluation = dateEvaluation; }

    public String getTypeEvaluation() { return typeEvaluation; }
    public void setTypeEvaluation(String typeEvaluation) { this.typeEvaluation = typeEvaluation; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
}
