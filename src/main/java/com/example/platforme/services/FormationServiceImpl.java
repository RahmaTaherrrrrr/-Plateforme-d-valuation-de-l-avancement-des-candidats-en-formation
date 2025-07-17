package com.example.platforme.services;

import com.example.platforme.dtos.FormationDTO;
import com.example.platforme.mappers.FormationMapper;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Utilisateur;
import com.example.platforme.repositories.FormationRepository;
import com.example.platforme.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormationServiceImpl implements FormationService {

    private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FormationMapper formationMapper;

    // Constructeur explicite pour injection
    public FormationServiceImpl(FormationRepository formationRepository,
                                UtilisateurRepository utilisateurRepository,
                                FormationMapper formationMapper) {
        this.formationRepository = formationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.formationMapper = formationMapper;
    }

    @Override
    public List<FormationDTO> getAll() {
        return formationRepository.findAll()
                .stream()
                .map(formationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormationDTO getById(Long id) {
        return formationRepository.findById(id)
                .map(formationMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + id));
    }

    @Override
    public FormationDTO create(FormationDTO dto) {
        if (formationRepository.existsByTitre(dto.getTitre())) {
            throw new IllegalArgumentException("Une formation avec ce titre existe déjà");
        }

        Utilisateur formateur = getCurrentUser();
        if (formateur == null) {
            throw new IllegalArgumentException("Aucun formateur authentifié trouvé");
        }

        Formation formation = formationMapper.toEntity(dto, formateur);
        return formationMapper.toDTO(formationRepository.save(formation));
    }

    @Override
    public FormationDTO update(Long id, FormationDTO dto) {
        Formation existing = formationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + id));

        Utilisateur formateur = getCurrentUser();
        if (formateur == null) {
            throw new IllegalArgumentException("Aucun formateur authentifié trouvé");
        }

        formationMapper.updateEntity(existing, dto, formateur);
        return formationMapper.toDTO(formationRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!formationRepository.existsById(id)) {
            throw new EntityNotFoundException("Formation non trouvée avec l'ID : " + id);
        }
        formationRepository.deleteById(id);
    }

    private Utilisateur getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByEmail(email).orElse(null);
    }
}

