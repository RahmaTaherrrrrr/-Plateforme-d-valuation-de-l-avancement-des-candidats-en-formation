package com.example.platforme.controllers;

import com.example.platforme.dtos.EvaluationDTO;
import com.example.platforme.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping
    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/{id}")
    public EvaluationDTO getEvaluationById(@PathVariable Integer id) {
        return evaluationService.getEvaluationById(id);
    }

    @PostMapping
    public EvaluationDTO createEvaluation(@RequestBody EvaluationDTO evaluationDTO) {
        return evaluationService.createEvaluation(evaluationDTO);
    }

    @PutMapping("/{id}")
    public EvaluationDTO updateEvaluation(@PathVariable Integer id, @RequestBody EvaluationDTO evaluationDTO) {
        return evaluationService.updateEvaluation(id, evaluationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEvaluation(@PathVariable Integer id) {
        evaluationService.deleteEvaluation(id);
    }
}
