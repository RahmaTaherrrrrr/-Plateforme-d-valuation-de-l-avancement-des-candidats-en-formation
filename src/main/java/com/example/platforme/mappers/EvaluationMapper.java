package com.example.platforme.mappers;

import com.example.platforme.dtos.EvaluationDTO;
import com.example.platforme.models.Evaluation;
import com.example.platforme.models.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    @Mapping(target = "moduleId", source = "module.id")
    EvaluationDTO toDTO(Evaluation evaluation);

    @Mapping(target = "module", ignore = true)
    @Mapping(target = "id", source = "evaluationDTO.id")
    Evaluation toEntity(EvaluationDTO evaluationDTO);

    default Evaluation toEntity(EvaluationDTO dto, Module module) {
        Evaluation evaluation = toEntity(dto);
        evaluation.setModule(module);
        return evaluation;
    }
}
