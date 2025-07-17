package com.example.platforme.mappers;

import com.example.platforme.dtos.ModuleDTO;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module;

public class ModuleMapper {

    public static ModuleDTO toDTO(Module m) {
        if (m == null) return null;
        ModuleDTO dto = new ModuleDTO();
        dto.setId(m.getId());
        dto.setTitre(m.getTitre());
        dto.setDescription(m.getDescription());
        dto.setDuree(m.getDuree());
        if (m.getFormation() != null) {
            dto.setFormationId(m.getFormation().getId());
        }
        return dto;
    }

    public static Module toEntity(ModuleDTO dto, Formation formation) {
        if (dto == null) return null;
        Module m = new Module();
        m.setId(dto.getId());
        m.setTitre(dto.getTitre());
        m.setDescription(dto.getDescription());
        m.setDuree(dto.getDuree());
        m.setFormation(formation);
        return m;
    }
}
