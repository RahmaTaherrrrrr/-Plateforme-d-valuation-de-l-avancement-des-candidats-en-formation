package com.example.platforme.mappers;

import com.example.platforme.dtos.UtilisateurCreateDTO;
import com.example.platforme.dtos.UtilisateurDTO;
import com.example.platforme.models.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UtilisateurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "formations", ignore = true)
    @Mapping(target = "role", expression = "java(com.example.platforme.models.Role.valueOf(dto.getRole()))")
    @Mapping(target = "password", ignore = true)
    Utilisateur toEntity(UtilisateurCreateDTO dto);

    @Mapping(target = "role", expression = "java(utilisateur.getRole().name())")
    UtilisateurDTO toDTO(Utilisateur utilisateur);
}
