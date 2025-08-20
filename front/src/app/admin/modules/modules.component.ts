// src/app/admin/modules/modules.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs'; // ✅ Importer forkJoin

import { Formation } from '../../models/formation.model';
import { Module } from '../../models/module.model';
import { ModuleService } from '../../services/module.service';
import { FormationService } from '../../services/formation.service';

@Component({
  selector: 'app-modules',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './modules.component.html',
})
export class ModulesComponent implements OnInit {
  modules: Module[] = [];
  formations: Formation[] = [];

  moduleForm: FormGroup;
  isEditing = false;
  currentModuleId: number | null = null;
  isLoading = false;
  errorMessage: string | null = null;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private moduleService: ModuleService,
    private formationService: FormationService
  ) {
    this.moduleForm = this.fb.group({
      titre: ['', [Validators.required, Validators.maxLength(100)]],
      duree: [0, [Validators.required, Validators.min(1)]], // ✅ Ajout du champ 'duree' manquant
      description: ['', Validators.maxLength(500)],
      formationId: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadInitialData(); // ✅ On utilise une seule méthode pour tout charger
  }

  // ----- DÉBUT DE LA CORRECTION -----
  loadInitialData(): void {
    this.isLoading = true;
    // On attend que les deux appels soient terminés
    forkJoin({
      formations: this.formationService.getFormations(),
      modules: this.moduleService.getAllModules()
    }).subscribe({
      next: ({ formations, modules }) => {
        this.formations = formations;
        
        // On enrichit chaque module avec l'objet formation complet
        this.modules = modules.map((module: any) => {
          return {
            ...module,
            formation: this.formations.find(f => f.id === module.formationId)
          };
        });
        
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = "Erreur lors du chargement des données.";
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  // Méthode pour rafraîchir uniquement les modules après une action
  refreshModules(): void {
    this.isLoading = true;
    this.moduleService.getAllModules().subscribe({
      next: (data) => {
        // On applique la même logique d'enrichissement
        this.modules = data.map((module: any) => ({
          ...module,
          formation: this.formations.find(f => f.id === module.formationId)
        }));
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = "Erreur lors du rafraîchissement des modules.";
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  // ----- FIN DE LA CORRECTION -----

  onSubmit(): void {
    this.submitted = true;
    if (this.moduleForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = null;

    const formValue = this.moduleForm.value;
    // On s'assure d'envoyer les données au format attendu par le backend (DTO)
    const moduleData = {
      titre: formValue.titre,
      duree: formValue.duree,
      description: formValue.description,
      formationId: formValue.formationId
    };

    const operation = this.isEditing && this.currentModuleId
      ? this.moduleService.updateModule(this.currentModuleId, moduleData)
      : this.moduleService.createModule(moduleData);

    operation.subscribe({
      next: () => {
        this.resetForm();
        this.refreshModules(); // ✅ On rafraîchit la liste
      },
      error: (err) => {
        this.errorMessage = `Erreur lors de ${this.isEditing ? 'la mise à jour' : 'la création'} du module.`;
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  editModule(module: Module): void {
    this.isEditing = true;
    this.currentModuleId = module.id!;
    this.submitted = false;

    this.moduleForm.patchValue({
      titre: module.titre,
      duree: module.duree,
      description: module.description,
      formationId: module.formation?.id
    });

    window.scrollTo(0, 0);
  }

  deleteModule(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce module ?")) {
      this.moduleService.deleteModule(id).subscribe({
        next: () => { this.refreshModules(); }, // ✅ On rafraîchit la liste
        error: (err) => {
          this.errorMessage = "Erreur lors de la suppression du module.";
          console.error(err);
        }
      });
    }
  }

  resetForm(): void {
    this.submitted = false;
    this.isEditing = false;
    this.currentModuleId = null;
    this.isLoading = false;
    this.errorMessage = null;
    this.moduleForm.reset();
    // On peut réinitialiser le selecteur de formation
    this.moduleForm.patchValue({ formationId: null });
  }
}
