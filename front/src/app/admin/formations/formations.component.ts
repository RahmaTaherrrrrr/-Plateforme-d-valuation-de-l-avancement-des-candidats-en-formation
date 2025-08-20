// src/app/components/formations/formations.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { forkJoin } from 'rxjs';

// Models
import { Formation, FormationDTO } from '../../models/formation.model';
import { Module } from '../../models/module.model';
import { Utilisateur } from '../../models/utilisateur.model';

// Services
import { FormationService } from '../../services/formation.service';
import { ModuleService } from '../../services/module.service';
import { UtilisateurService } from '../../services/utilisateur.service';

@Component({
  selector: 'app-formations',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [DatePipe],
  templateUrl: './formations.component.html',
})
export class FormationsComponent implements OnInit {

  formations: Formation[] = [];
  tousLesModules: Module[] = [];
  formateurs: Utilisateur[] = [];

  formationForm: FormGroup;
  isEditing = false;
  currentFormationId: number | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private formationService: FormationService,
    private moduleService: ModuleService,
    private utilisateurService: UtilisateurService,
    private datePipe: DatePipe
  ) {
    this.formationForm = this.fb.group({
      titre: ['', Validators.required],
      description: [''],
      formateurId: [null, Validators.required],
      moduleIds: [[]],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    this.errorMessage = null;

    forkJoin({
      formations: this.formationService.getFormations(),
      modules: this.moduleService.getAllModules(),
      formateurs: this.utilisateurService.getFormateurs()
    }).subscribe({
      next: (result) => {
        const allFormations = result.formations;
        const allModules = result.modules;

        
        allFormations.forEach(formation => {
     
          formation.modules = allModules.filter(
            module => module.formation?.id === formation.id
          );
        });

        this.formations = allFormations;
        this.tousLesModules = allModules;
        this.formateurs = result.formateurs;
      },
      error: (err) => {
        this.errorMessage = "Une erreur critique est survenue lors du chargement des données.";
        console.error("Erreur de chargement initial :", err);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.formationForm.invalid) {
      this.formationForm.markAllAsTouched();
      return;
    }

    const formationData: FormationDTO = {
      titre: this.formationForm.value.titre,
      description: this.formationForm.value.description,
      formateurId: this.formationForm.value.formateurId,
      moduleIds: this.formationForm.value.moduleIds || [],
      dateDebut: this.formationForm.value.dateDebut,
      dateFin: this.formationForm.value.dateFin
    };

    const operation = this.isEditing && this.currentFormationId
      ? this.formationService.updateFormation(this.currentFormationId, formationData)
      : this.formationService.createFormation(formationData);

    operation.subscribe({
      next: () => {
        this.resetForm();
      },
      error: (err) => {
        this.errorMessage = `Erreur lors de ${this.isEditing ? 'la mise à jour' : 'la création'}.`;
      }
    });
  }

  editFormation(formation: Formation): void {
    this.isEditing = true;
    this.currentFormationId = formation.id!;

    this.formationForm.patchValue({
      titre: formation.titre,
      description: formation.description,
      formateurId: formation.formateur?.id,
      moduleIds: formation.modules?.map(m => m.id) || [],
      dateDebut: this.datePipe.transform(formation.dateDebut, 'yyyy-MM-dd'),
      dateFin: this.datePipe.transform(formation.dateFin, 'yyyy-MM-dd')
    });
    window.scrollTo(0, 0);
  }

  deleteFormation(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette formation ?")) {
      this.formationService.deleteFormation(id).subscribe({
        next: () => this.loadInitialData(),
        error: (err) => this.errorMessage = "Erreur lors de la suppression."
      });
    }
  }

  resetForm(): void {
    this.isEditing = false;
    this.currentFormationId = null;
    this.formationForm.reset();
    this.loadInitialData(); 
  }


  findFirstModuleName(formationId: number | null): string {
    if (!formationId) return 'N/A';
    const foundModule = this.tousLesModules.find(m => m.formation?.id === formationId);
    return foundModule ? foundModule.titre : 'Aucun';
  }
}
