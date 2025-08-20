package com.example.platforme.controllers;

import com.example.platforme.dtos.FormationDTO;
import com.example.platforme.services.FormationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Formations", description = "API pour la gestion des formations")
@RestController
@RequestMapping("/api/formations")
public class FormationController {

    private final FormationService formationService;

    @Autowired
    public FormationController(FormationService formationService) {
        this.formationService = formationService;
    }

    @Operation(summary = "Récupérer toutes les formations",
            description = "Retourne la liste de toutes les formations. Accessible à tous les utilisateurs authentifiés.",
            security = @SecurityRequirement(name = "formLogin"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des formations récupérée avec succès")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<FormationDTO> getAll() {
        return formationService.getAll();
    }

    @Operation(summary = "Récupérer une formation par ID",
            description = "Retourne les détails d'une formation spécifique. Accessible à tous les utilisateurs authentifiés.",
            security = @SecurityRequirement(name = "formLogin"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formation trouvée"),
            @ApiResponse(responseCode = "404", description = "Formation non trouvée")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public FormationDTO getById(@PathVariable Long id) {
        return formationService.getById(id);
    }

    @Operation(summary = "Créer une nouvelle formation",
            description = "Crée une formation pour un formateur spécifié. Nécessite le rôle FORMATEUR.",
            security = @SecurityRequirement(name = "formLogin"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Formation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (rôle FORMATEUR requis)"),
            @ApiResponse(responseCode = "409", description = "Une formation avec ce titre existe déjà")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormationDTO create(@Valid @RequestBody FormationDTO formationDTO) {
        return formationService.create(formationDTO);
    }


    @Operation(summary = "Mettre à jour une formation",
            description = "Met à jour une formation existante. Nécessite le rôle FORMATEUR.",
            security = @SecurityRequirement(name = "formLogin"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formation mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (rôle FORMATEUR requis)"),
            @ApiResponse(responseCode = "404", description = "Formation non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public FormationDTO update(@PathVariable Long id, @Valid @RequestBody FormationDTO formationDTO) {
        return formationService.update(id, formationDTO);
    }

    @Operation(summary = "Supprimer une formation",
            description = "Supprime une formation existante. Nécessite le rôle FORMATEUR.",
            security = @SecurityRequirement(name = "formLogin"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Formation supprimée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (rôle FORMATEUR requis)"),
            @ApiResponse(responseCode = "404", description = "Formation non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        formationService.delete(id);
    }
}