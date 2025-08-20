package com.example.platforme.dtos;

public class JwtAuthenticationResponse {

    private String token;
    private UtilisateurDTO utilisateur; // <-- Ajoutez ce champ

    public JwtAuthenticationResponse(String token, UtilisateurDTO utilisateur) { // <-- Mettez à jour le constructeur
        this.token = token;
        this.utilisateur = utilisateur;
    }

    // Getters et Setters pour les deux champs
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }
}
