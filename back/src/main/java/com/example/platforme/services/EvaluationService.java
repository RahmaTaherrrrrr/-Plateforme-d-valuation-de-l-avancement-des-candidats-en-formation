package com.example.platforme.services;

import com.example.platforme.dtos.EvaluationDTO;
import java.util.List;

public interface EvaluationService {
    List<EvaluationDTO> getAllEvaluations();
    EvaluationDTO getEvaluationById(Integer id);
    EvaluationDTO createEvaluation(EvaluationDTO evaluationDTO);
    EvaluationDTO updateEvaluation(Integer id, EvaluationDTO evaluationDTO);
    void deleteEvaluation(Integer id);
}


