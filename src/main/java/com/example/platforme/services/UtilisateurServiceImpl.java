package com.example.platforme.services;

import com.example.platforme.dtos.UtilisateurCreateDTO;
import com.example.platforme.dtos.UtilisateurDTO;
import com.example.platforme.mappers.UtilisateurMapper;
import com.example.platforme.models.Role;
import com.example.platforme.models.Utilisateur;
import com.example.platforme.repositories.UtilisateurRepository;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurServiceImpl.class);
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  UtilisateurMapper utilisateurMapper,
                                  PasswordEncoder passwordEncoder,
                                  Validator validator) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }
    public UtilisateurDTO create(UtilisateurCreateDTO utilisateurCreateDTO) {
        // Validation des données
        var violations = validator.validate(utilisateurCreateDTO);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Données invalides : " + violations);
        }

        // Vérification de l'unicité de l'email
        if (utilisateurRepository.findByEmail(utilisateurCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà : " + utilisateurCreateDTO.getEmail());
        }

        // Dans la méthode create() de UtilisateurServiceImpl.java

// ...
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurCreateDTO);
        utilisateur.setPassword(passwordEncoder.encode(utilisateurCreateDTO.getPassword()));

// ======================= LOGIQUE DE RÔLE AVANCÉE =======================
// On vérifie si un rôle a été passé dans la requête.
        String roleDemandee = utilisateurCreateDTO.getRole();

        if (roleDemandee != null && !roleDemandee.isBlank()) {
            // Un rôle a été demandé. On essaie de l'assigner.
            try {
                // On convertit la chaîne de caractères en Enum. ex: "FORMATEUR" -> Role.FORMATEUR
                utilisateur.setRole(Role.valueOf(roleDemandee.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Si le rôle demandé n'existe pas (ex: "SUPERMAN"), on assigne CANDIDAT par sécurité.
                logger.warn("Rôle invalide demandé : '{}'. Assignation du rôle CANDIDAT par défaut.", roleDemandee);
                utilisateur.setRole(Role.CANDIDAT);
            }
        } else {
            // Aucun rôle n'a été demandé (cas de l'inscription publique standard).
            // On assigne CANDIDAT par défaut.
            utilisateur.setRole(Role.CANDIDAT);
        }
// =======================================================================

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
// ...


        logger.info("Utilisateur créé avec succès par {} : {}",
                SecurityContextHolder.getContext().getAuthentication() != null ?
                        SecurityContextHolder.getContext().getAuthentication().getName() : "ANONYME",
                savedUtilisateur.getEmail());
        return utilisateurMapper.toDTO(savedUtilisateur);
    }


}