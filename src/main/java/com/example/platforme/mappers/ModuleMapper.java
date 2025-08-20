package com.example.platforme.mappers;

import com.example.platforme.dtos.ModuleDTO;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module;

/**
 * Mapper pour la conversion entre Module et ModuleDTO
 * Correction : Utilisation de méthodes statiques au lieu d'interface MapStruct
 */
public class ModuleMapper {

    /**
     * Convertit un Module en ModuleDTO
     */
    public static ModuleDTO toDTO(Module module) {
        if (module == null) {
            return null;
        }

        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setTitre(module.getTitre());
        dto.setDescription(module.getDescription());
        dto.setDuree(module.getDuree());

        // Gestion de la formation
        if (module.getFormation() != null) {
            dto.setFormationId(module.getFormation().getId());
            dto.setFormationTitre(module.getFormation().getTitre());
        }

        return dto;
    }

    /**
     * Convertit un ModuleDTO en Module avec une Formation
     */
    public static Module toEntity(ModuleDTO dto, Formation formation) {
        if (dto == null) {
            return null;
        }

        Module module = new Module();
        module.setId(dto.getId());
        module.setTitre(dto.getTitre());
        module.setDescription(dto.getDescription());
        module.setDuree(dto.getDuree());
        module.setFormation(formation);

        return module;
    }

    /**
     * Met à jour un Module existant avec les données d'un ModuleDTO
     */
    public static void updateEntityFromDto(Module module, ModuleDTO dto) {
        if (module == null || dto == null) {
            return;
        }

        module.setTitre(dto.getTitre());
        module.setDescription(dto.getDescription());
        module.setDuree(dto.getDuree());
        // La formation est gérée séparément dans le service
    }
}

