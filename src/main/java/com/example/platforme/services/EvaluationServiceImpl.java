package com.example.platforme.services;

import com.example.platforme.dtos.EvaluationDTO;
import com.example.platforme.mappers.EvaluationMapper;
import com.example.platforme.models.Evaluation;
import com.example.platforme.models.Module;
import com.example.platforme.repositories.EvaluationRepository;
import com.example.platforme.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationDTO getEvaluationById(Integer id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Évaluation non trouvée avec id " + id));
        return evaluationMapper.toDTO(evaluation);
    }

    @Override
    public EvaluationDTO createEvaluation(EvaluationDTO dto) {
        Module module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new EntityNotFoundException("Module non trouvé avec id " + dto.getModuleId()));

        Evaluation evaluation = evaluationMapper.toEntity(dto, module);
        Evaluation saved = evaluationRepository.save(evaluation);
        return evaluationMapper.toDTO(saved);
    }

    @Override
    public EvaluationDTO updateEvaluation(Integer id, EvaluationDTO dto) {
        Evaluation existing = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Évaluation non trouvée avec id " + id));

        Module module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new EntityNotFoundException("Module non trouvé avec id " + dto.getModuleId()));

        // Mise à jour des champs
        existing.setDateEvaluation(dto.getDateEvaluation());
        existing.setTypeEvaluation(dto.getTypeEvaluation());
        existing.setDuree(dto.getDuree());
        existing.setModule(module);

        Evaluation updated = evaluationRepository.save(existing);
        return evaluationMapper.toDTO(updated);
    }

    @Override
    public void deleteEvaluation(Integer id) {
        evaluationRepository.deleteById(id);
    }
}
