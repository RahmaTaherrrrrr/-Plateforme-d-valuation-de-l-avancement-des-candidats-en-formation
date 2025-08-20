package com.example.platforme.controllers;

import com.example.platforme.dtos.ModuleDTO;
import com.example.platforme.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public List<ModuleDTO> getAllModules() {
        return moduleService.getAllModules();
    }

    @GetMapping("/{id}")
    public ModuleDTO getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }

    @PostMapping
    public ModuleDTO createModule(@RequestBody ModuleDTO moduleDTO) {
        return moduleService.createModule(moduleDTO);
    }

    @PutMapping("/{id}")
    public ModuleDTO updateModule(@PathVariable Long id, @RequestBody ModuleDTO moduleDTO) {
        return moduleService.updateModule(id, moduleDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
    }
}
