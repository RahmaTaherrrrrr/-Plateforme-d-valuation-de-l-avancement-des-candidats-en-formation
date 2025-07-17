package com.example.platforme.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UtilisateurCreateDTO {

    @NotNull(message = "Le nom ne peut pas être nul")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String nom;

    @NotNull(message = "Le prénom ne peut pas être nul")
    @Size(min = 3, max = 50, message = "Le prénom doit contenir entre 3 et 50 caractères")
    private String prenom;

    @NotNull(message = "L'email ne peut pas être nul")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @Size(max = 15, message = "Le numéro de téléphone ne doit pas dépasser 15 caractères")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Le numéro de téléphone doit être valide")
    private String telephone;

    @NotNull(message = "Le mot de passe ne peut pas être nul")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Le mot de passe doit contenir des lettres et des chiffres")
    private String password;

    @NotNull(message = "Le rôle ne peut pas être nul")
    @Pattern(regexp = "ADMIN|USER|FORMATEUR", message = "Le rôle doit être ADMIN, USER ou FORMATEUR")
    private String role;

    public UtilisateurCreateDTO() {}

    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}