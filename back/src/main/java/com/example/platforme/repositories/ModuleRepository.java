package com.example.platforme.repositories;

import com.example.platforme.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des modules - Version corrigée
 * Corrections : Ajout de méthodes de recherche personnalisées
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    /**
     * Trouve tous les modules d'une formation donnée
     */
    List<Module> findByFormationId(Long formationId);

    /**
     * Trouve tous les modules d'une formation donnée, triés par titre
     */
    List<Module> findByFormationIdOrderByTitre(Long formationId);

    /**
     * Vérifie si un module avec ce titre existe dans cette formation
     */
    boolean existsByTitreAndFormationId(String titre, Long formationId);

    /**
     * Vérifie si un module avec ce titre existe dans cette formation (excluant un ID donné)
     */
    boolean existsByTitreAndFormationIdAndIdNot(String titre, Long formationId, Long id);

    /**
     * Trouve les modules par titre (recherche partielle, insensible à la casse)
     */
    @Query("SELECT m FROM Module m WHERE LOWER(m.titre) LIKE LOWER(CONCAT('%', :titre, '%'))")
    List<Module> findByTitreContainingIgnoreCase(@Param("titre") String titre);

    /**
     * Trouve les modules par formation et durée minimale
     */
    @Query("SELECT m FROM Module m WHERE m.formation.id = :formationId AND m.duree >= :dureeMin")
    List<Module> findByFormationIdAndDureeGreaterThanEqual(@Param("formationId") Long formationId, @Param("dureeMin") Integer dureeMin);

    /**
     * Calcule la durée totale des modules d'une formation
     */
    @Query("SELECT COALESCE(SUM(m.duree), 0) FROM Module m WHERE m.formation.id = :formationId")
    Integer calculateTotalDurationByFormationId(@Param("formationId") Long formationId);

    /**
     * Compte le nombre de modules dans une formation
     */
    long countByFormationId(Long formationId);

    /**
     * Supprime tous les modules d'une formation
     */
    void deleteByFormationId(Long formationId);
}

