package com.example.platforme.services;

import com.example.platforme.dtos.ModuleDTO;

import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getAllModules();
    ModuleDTO getModuleById(Long id);
    ModuleDTO createModule(ModuleDTO dto);
    ModuleDTO updateModule(Long id, ModuleDTO dto);
    void deleteModule(Long id);
}
