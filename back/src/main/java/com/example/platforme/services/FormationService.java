package com.example.platforme.services;

import com.example.platforme.dtos.FormationDTO;

import java.util.List;



public interface FormationService {
    List<FormationDTO> getAll();
    FormationDTO getById(Long id);
    FormationDTO create(FormationDTO dto);
    FormationDTO update(Long id, FormationDTO dto);
    void delete(Long id);
}