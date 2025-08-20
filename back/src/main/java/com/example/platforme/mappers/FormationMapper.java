// Fichier : com/example/platforme/mappers/FormationMapper.java

package com.example.platforme.mappers;

import com.example.platforme.dtos.FormationDTO;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Module; // <-- Import nécessaire
import com.example.platforme.models.Utilisateur;
import org.mapstruct.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List; // <-- Import nécessaire
import java.util.stream.Collectors; // <-- Import nécessaire

@Mapper(componentModel = "spring")
public interface FormationMapper {

    String DATE_PATTERN = "yyyy-MM-dd";

    // ======================= CORRECTION DE LA MÉTHODE toEntity =======================
    // On ignore les modules ici car ils seront gérés séparément dans le service.
    @Mapping(target = "formateur", source = "formateur")
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateDebut", source = "dto.dateDebut", qualifiedByName = "stringToDate")
    @Mapping(target = "dateFin", source = "dto.dateFin", qualifiedByName = "stringToDate")
    Formation toEntity(FormationDTO dto, Utilisateur formateur);

    // ======================= CORRECTION DE LA MÉTHODE toDTO =======================
    // On ajoute le mapping pour les moduleIds.
    @Mapping(target = "formateurId", source = "formateur.id")
    @Mapping(target = "formateurNom", source = "formateur.nom")
    @Mapping(target = "dateDebut", source = "dateDebut", qualifiedByName = "dateToString")
    @Mapping(target = "dateFin", source = "dateFin", qualifiedByName = "dateToString")
    @Mapping(target = "moduleIds", source = "modules", qualifiedByName = "modulesToIds") // <-- AJOUT IMPORTANT
    FormationDTO toDTO(Formation formation);

    // ======================= NOUVELLE MÉTHODE POUR LA MISE À JOUR =======================
    /**
     * Met à jour une entité Formation à partir d'un DTO.
     * On ignore l'ID, les modules et le formateur car ils sont gérés dans le service.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "formateur", ignore = true)
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "dateDebut", source = "dateDebut", qualifiedByName = "stringToDate")
    @Mapping(target = "dateFin", source = "dateFin", qualifiedByName = "stringToDate")
    void updateEntityFromDto(@MappingTarget Formation entity, FormationDTO dto);

    // ======================= MÉTHODES UTILITAIRES =======================

    @Named("stringToDate")
    default Date stringToDate(String dateStr) {
        try {
            return dateStr == null ? null : new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide : " + dateStr, e);
        }
    }

    @Named("dateToString")
    default String dateToString(Date date) {
        return date == null ? null : new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    // Nouvelle méthode pour convertir une liste de Modules en une liste d'IDs
    @Named("modulesToIds")
    default List<Long> modulesToIds(List<Module> modules) {
        if (modules == null) {
            return null;
        }
        return modules.stream()
                .map(Module::getId)
                .collect(Collectors.toList());
    }
}
