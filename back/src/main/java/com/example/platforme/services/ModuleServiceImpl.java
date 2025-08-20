package com.example.platforme.services;

import com.example.platforme.dtos.ModuleDTO;
import com.example.platforme.mappers.ModuleMapper;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module;
import com.example.platforme.repositories.FormationRepository;
import com.example.platforme.repositories.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des modules - Version corrigée
 * Corrections : Gestion des transactions, validation des données, gestion des erreurs
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final FormationRepository formationRepository;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository, FormationRepository formationRepository) {
        this.moduleRepository = moduleRepository;
        this.formationRepository = formationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDTO> getAllModules() {
        try {
            return moduleRepository.findAll()
                    .stream()
                    .map(ModuleMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des modules", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleDTO getModuleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du module ne peut pas être null");
        }

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Module non trouvé avec l'ID : " + id));

        return ModuleMapper.toDTO(module);
    }

    @Override
    @Transactional
    public ModuleDTO createModule(ModuleDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le DTO du module ne peut pas être null");
        }

        // Validation des données
        validateModuleDTO(dto);

        // Vérification de l'existence de la formation
        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + dto.getFormationId()));

        // Vérification de l'unicité du titre dans la formation
        if (moduleRepository.existsByTitreAndFormationId(dto.getTitre(), dto.getFormationId())) {
            throw new IllegalArgumentException("Un module avec ce titre existe déjà dans cette formation");
        }

        try {
            Module module = ModuleMapper.toEntity(dto, formation);
            Module savedModule = moduleRepository.save(module);
            return ModuleMapper.toDTO(savedModule);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du module", e);
        }
    }

    @Override
    @Transactional
    public ModuleDTO updateModule(Long id, ModuleDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du module ne peut pas être null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Le DTO du module ne peut pas être null");
        }

        // Validation des données
        validateModuleDTO(dto);

        Module existingModule = moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Module non trouvé avec l'ID : " + id));

        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + dto.getFormationId()));

        // Vérification de l'unicité du titre (sauf pour le module actuel)
        if (moduleRepository.existsByTitreAndFormationIdAndIdNot(dto.getTitre(), dto.getFormationId(), id)) {
            throw new IllegalArgumentException("Un autre module avec ce titre existe déjà dans cette formation");
        }

        try {
            // Mise à jour des champs
            ModuleMapper.updateEntityFromDto(existingModule, dto);
            existingModule.setFormation(formation);

            Module updatedModule = moduleRepository.save(existingModule);
            return ModuleMapper.toDTO(updatedModule);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du module", e);
        }
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du module ne peut pas être null");
        }

        if (!moduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Module non trouvé avec l'ID : " + id);
        }

        try {
            moduleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du module", e);
        }
    }

    /**
     * Valide les données d'un ModuleDTO
     */
    private void validateModuleDTO(ModuleDTO dto) {
        if (dto.getTitre() == null || dto.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du module est requis");
        }
        if (dto.getTitre().length() > 100) {
            throw new IllegalArgumentException("Le titre du module ne doit pas dépasser 100 caractères");
        }
        if (dto.getDescription() != null && dto.getDescription().length() > 500) {
            throw new IllegalArgumentException("La description du module ne doit pas dépasser 500 caractères");
        }
        if (dto.getDuree() == null || dto.getDuree() <= 0) {
            throw new IllegalArgumentException("La durée du module doit être positive");
        }
        if (dto.getFormationId() == null) {
            throw new IllegalArgumentException("L'ID de la formation est requis");
        }
    }
}

