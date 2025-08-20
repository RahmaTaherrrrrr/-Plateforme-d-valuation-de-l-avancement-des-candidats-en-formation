package com.example.platforme.controllers;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.platforme.dtos.UtilisateurCreateDTO;
import com.example.platforme.dtos.UtilisateurDTO;
import com.example.platforme.services.UtilisateurServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Utilisateurs", description = "API pour la gestion des utilisateurs")
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurServiceImpl utilisateurService;

    public UtilisateurController(UtilisateurServiceImpl utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Créer un nouvel utilisateur",
            description = "Crée un utilisateur avec les informations fournies. Nécessite le rôle ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (rôle ADMIN requis)"),
            @ApiResponse(responseCode = "409", description = "L'email existe déjà")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurDTO create(@Valid @RequestBody UtilisateurCreateDTO utilisateurCreateDTO) {
        return utilisateurService.create(utilisateurCreateDTO);
    }
}
