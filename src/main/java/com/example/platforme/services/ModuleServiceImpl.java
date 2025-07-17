package com.example.platforme.services;

import com.example.platforme.dtos.ModuleDTO;
import com.example.platforme.mappers.ModuleMapper;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module;
import com.example.platforme.repositories.FormationRepository;
import com.example.platforme.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public List<ModuleDTO> getAllModules() {
        return moduleRepository.findAll()
                .stream()
                .map(ModuleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleDTO getModuleById(Long id) {
        return moduleRepository.findById(id)
                .map(ModuleMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Module non trouvé"));
    }

    @Override
    public ModuleDTO createModule(ModuleDTO dto) {
        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        Module module = ModuleMapper.toEntity(dto, formation);
        return ModuleMapper.toDTO(moduleRepository.save(module));
    }

    @Override
    public ModuleDTO updateModule(Long id, ModuleDTO dto) {
        Module existing = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module non trouvé"));

        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        existing.setTitre(dto.getTitre());
        existing.setDescription(dto.getDescription());
        existing.setFormation(formation);

        return ModuleMapper.toDTO(moduleRepository.save(existing));
    }

    @Override
    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }
}
