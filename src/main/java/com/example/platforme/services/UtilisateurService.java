package com.example.platforme.services;

import com.example.platforme.dtos.UtilisateurCreateDTO;
import com.example.platforme.dtos.UtilisateurDTO;

import java.util.List;

public interface UtilisateurService {
    List<UtilisateurDTO> getAll();
    UtilisateurDTO getById(Long id);
    UtilisateurDTO create(UtilisateurCreateDTO dto);
    void delete(Long id);
}
