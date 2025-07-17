package com.example.platforme.repositories;

import com.example.platforme.models.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    boolean existsByTitre(String titre);
}

