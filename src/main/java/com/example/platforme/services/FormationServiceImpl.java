package com.example.platforme.services;

import com.example.platforme.dtos.FormationDTO;
import com.example.platforme.mappers.FormationMapper;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module;
import com.example.platforme.models.Utilisateur;
import com.example.platforme.repositories.FormationRepository;
import com.example.platforme.repositories.ModuleRepository;
import com.example.platforme.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des formations - Version corrigée
 * Corrections : Gestion complète des relations avec les modules, validation des données
 */
@Service
@Transactional
public class FormationServiceImpl implements FormationService {

    private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ModuleRepository moduleRepository;
    private final FormationMapper formationMapper;

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    @Autowired
    public FormationServiceImpl(FormationRepository formationRepository,
                                UtilisateurRepository utilisateurRepository,
                                ModuleRepository moduleRepository,
                                FormationMapper formationMapper) {
        this.formationRepository = formationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.moduleRepository = moduleRepository;
        this.formationMapper = formationMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormationDTO> getAll() {
        try {
            return formationRepository.findAll()
                    .stream()
                    .map(formationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des formations", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FormationDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la formation ne peut pas être null");
        }

        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + id));

        return formationMapper.toDTO(formation);
    }

    @Override
    @Transactional
    public FormationDTO create(FormationDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le DTO de la formation ne peut pas être null");
        }

        // Validation des données
        validateFormationDTO(dto);

        // Vérification de l'unicité du titre
        if (formationRepository.existsByTitre(dto.getTitre())) {
            throw new IllegalArgumentException("Une formation avec ce titre existe déjà");
        }

        // Récupération du formateur
        Utilisateur formateur = utilisateurRepository.findById(dto.getFormateurId())
                .orElseThrow(() -> new EntityNotFoundException("Formateur non trouvé avec l'ID : " + dto.getFormateurId()));

        try {
            // Création de l'entité Formation
            Formation formation = formationMapper.toEntity(dto, formateur);

            // Gestion des modules si des IDs sont fournis
            if (dto.getModuleIds() != null && !dto.getModuleIds().isEmpty()) {
                List<Module> modules = moduleRepository.findAllById(dto.getModuleIds());
                if (modules.size() != dto.getModuleIds().size()) {
                    throw new EntityNotFoundException("Un ou plusieurs modules n'ont pas été trouvés.");
                }

                // Établissement de la relation bidirectionnelle
                modules.forEach(module -> module.setFormation(formation));
                formation.setModules(modules);
            } else {
                formation.setModules(Collections.emptyList());
            }

            Formation savedFormation = formationRepository.save(formation);
            return formationMapper.toDTO(savedFormation);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la formation", e);
        }
    }

    @Override
    @Transactional
    public FormationDTO update(Long id, FormationDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la formation ne peut pas être null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Le DTO de la formation ne peut pas être null");
        }

        // Validation des données
        validateFormationDTO(dto);

        Formation existingFormation = formationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + id));

        // Vérification de l'unicité du titre (sauf pour la formation actuelle)
        if (formationRepository.existsByTitreAndIdNot(dto.getTitre(), id)) {
            throw new IllegalArgumentException("Une autre formation avec ce titre existe déjà");
        }

        Utilisateur formateur = utilisateurRepository.findById(dto.getFormateurId())
                .orElseThrow(() -> new EntityNotFoundException("Formateur non trouvé avec l'ID : " + dto.getFormateurId()));

        try {
            // Mise à jour des champs de base
            formationMapper.updateEntityFromDto(existingFormation, dto);
            existingFormation.setFormateur(formateur);

            // Gestion des modules
            updateFormationModules(existingFormation, dto.getModuleIds());

            Formation updatedFormation = formationRepository.save(existingFormation);
            return formationMapper.toDTO(updatedFormation);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la formation", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la formation ne peut pas être null");
        }

        if (!formationRepository.existsById(id)) {
            throw new EntityNotFoundException("Formation non trouvée avec l'ID : " + id);
        }

        try {
            // Suppression en cascade des modules associés
            moduleRepository.deleteByFormationId(id);
            formationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la formation", e);
        }
    }

    /**
     * Met à jour les modules associés à une formation
     */
    private void updateFormationModules(Formation formation, List<Long> moduleIds) {
        // Suppression des anciennes relations
        if (formation.getModules() != null) {
            formation.getModules().forEach(module -> module.setFormation(null));
            formation.getModules().clear();
        }

        // Ajout des nouvelles relations
        if (moduleIds != null && !moduleIds.isEmpty()) {
            List<Module> modules = moduleRepository.findAllById(moduleIds);
            if (modules.size() != moduleIds.size()) {
                throw new EntityNotFoundException("Un ou plusieurs modules n'ont pas été trouvés.");
            }

            modules.forEach(module -> module.setFormation(formation));
            formation.setModules(modules);
        }
    }

    /**
     * Valide les données d'un FormationDTO
     */
    private void validateFormationDTO(FormationDTO dto) {
        if (dto.getTitre() == null || dto.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la formation est requis");
        }
        if (dto.getTitre().length() > 100) {
            throw new IllegalArgumentException("Le titre de la formation ne doit pas dépasser 100 caractères");
        }
        if (dto.getDescription() != null && dto.getDescription().length() > 500) {
            throw new IllegalArgumentException("La description de la formation ne doit pas dépasser 500 caractères");
        }
        if (dto.getDateDebut() == null || dto.getDateDebut().trim().isEmpty()) {
            throw new IllegalArgumentException("La date de début est requise");
        }
        if (dto.getDateFin() == null || dto.getDateFin().trim().isEmpty()) {
            throw new IllegalArgumentException("La date de fin est requise");
        }
        if (dto.getFormateurId() == null) {
            throw new IllegalArgumentException("L'ID du formateur est requis");
        }

        // Validation des dates
        try {
            Date dateDebut = dateFormat.parse(dto.getDateDebut());
            Date dateFin = dateFormat.parse(dto.getDateFin());

            if (dateFin.before(dateDebut)) {
                throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format yyyy-MM-dd");
        }
    }
}

