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

        // Conversion et enregistrement
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurCreateDTO);
        utilisateur.setPassword(passwordEncoder.encode(utilisateurCreateDTO.getPassword()));

        utilisateur.setRole(Role.ADMIN); // ← on force le rôle ici

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        logger.info("Utilisateur créé avec succès par {} : {}",
                SecurityContextHolder.getContext().getAuthentication() != null ?
                        SecurityContextHolder.getContext().getAuthentication().getName() : "ANONYME",
                savedUtilisateur.getEmail());
        return utilisateurMapper.toDTO(savedUtilisateur);
    }


}