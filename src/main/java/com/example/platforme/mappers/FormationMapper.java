package com.example.platforme.mappers;

import com.example.platforme.dtos.FormationDTO;
import com.example.platforme.models.Formation;
import com.example.platforme.models.Utilisateur;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface FormationMapper {

    String DATE_PATTERN = "yyyy-MM-dd"; // Format des dates utilisées dans les DTO

    // 🔁 DTO -> Entity
    @Mapping(target = "formateur", source = "formateur")
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateDebut", source = "dto.dateDebut", qualifiedByName = "stringToDate")
    @Mapping(target = "dateFin", source = "dto.dateFin", qualifiedByName = "stringToDate")
    Formation toEntity(FormationDTO dto, Utilisateur formateur);

    // 🔁 Entity -> DTO
    @Mapping(target = "formateurId", source = "formateur.id")
    @Mapping(target = "formateurNom", source = "formateur.nom")
    @Mapping(target = "dateDebut", source = "dateDebut", qualifiedByName = "dateToString")
    @Mapping(target = "dateFin", source = "dateFin", qualifiedByName = "dateToString")
    FormationDTO toDTO(Formation formation);

    // 🔁 Mise à jour d’une entité existante
    @Mapping(target = "formateur", source = "formateur")
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateDebut", source = "dto.dateDebut", qualifiedByName = "stringToDate")
    @Mapping(target = "dateFin", source = "dto.dateFin", qualifiedByName = "stringToDate")
    void updateEntity(@MappingTarget Formation entity, FormationDTO dto, Utilisateur formateur);

    // ======= MAPPINGS CUSTOM POUR DATES =======
    @Named("stringToDate")
    default Date stringToDate(String dateStr) {
        try {
            return dateStr == null ? null : new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide : " + dateStr);
        }
    }

    @Named("dateToString")
    default String dateToString(Date date) {
        return date == null ? null : new SimpleDateFormat(DATE_PATTERN).format(date);
    }
}
