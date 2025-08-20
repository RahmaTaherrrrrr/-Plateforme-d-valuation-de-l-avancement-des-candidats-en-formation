// src/app/components/evaluations/evaluations.component.ts

import { Component, OnInit } from '@angular/core';
import { Evaluation } from '../../models/evaluation.model';
import { Module } from '../../models/module.model';
import { EvaluationService } from '../../services/evaluation.service';
import { ModuleService } from '../../services/module.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common'; // ✅ 1. Importer DatePipe
import { ReactiveFormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-evaluation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [DatePipe], // ✅ 2. Fournir DatePipe pour pouvoir l'utiliser
  templateUrl: './evaluations.component.html'
})
export class EvaluationsComponent implements OnInit {
  evaluations: Evaluation[] = [];
  modules: Module[] = [];
  evaluationForm!: FormGroup;

  // ✅ 3. Déclarer les variables pour gérer l'état de l'édition
  isEditing = false;
  currentEvaluationId: number | null = null;

  constructor(
    private evaluationService: EvaluationService,
    private moduleService: ModuleService,
    private fb: FormBuilder,
    private datePipe: DatePipe // ✅ 4. Injecter DatePipe pour formater la date
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadInitialData();
  }

  initForm() {
    this.evaluationForm = this.fb.group({
      dateEvaluation: ['', Validators.required],
      typeEvaluation: ['', Validators.required],
      duree: ['', [Validators.required, Validators.min(0)]],
      moduleId: ['', Validators.required]
    });
  }

  loadInitialData() {
    forkJoin({
      modules: this.moduleService.getAllModules(),
      evaluations: this.evaluationService.getAllEvaluations()
    }).subscribe(({ modules, evaluations }) => {
      this.modules = modules;
      this.evaluations = evaluations.map((evaluation: any) => ({
        ...evaluation,
        module: this.modules.find(m => m.id === evaluation.moduleId)
      }));
    });
  }

  refreshEvaluations() {
    this.evaluationService.getAllEvaluations().subscribe((evaluations: any[]) => {
        this.evaluations = evaluations.map(evaluation => ({
            ...evaluation,
            module: this.modules.find(m => m.id === evaluation.moduleId)
        }));
    });
  }

  // ✅ ================== onSubmit MODIFIÉ POUR GÉRER L'UPDATE ==================
  onSubmit() {
    if (this.evaluationForm.invalid) {
      this.evaluationForm.markAllAsTouched();
      return;
    }

    const formValues = this.evaluationForm.value;
    const evaluationPayload = {
      dateEvaluation: formValues.dateEvaluation,
      typeEvaluation: formValues.typeEvaluation,
      duree: formValues.duree,
      moduleId: +formValues.moduleId
    };

    let operation$;

    if (this.isEditing && this.currentEvaluationId !== null) {
      // --- MODE UPDATE ---
      operation$ = this.evaluationService.updateEvaluation(this.currentEvaluationId, evaluationPayload);
    } else {
      // --- MODE CREATE ---
      operation$ = this.evaluationService.createEvaluation(evaluationPayload);
    }

    operation$.subscribe({
      next: () => {
        console.log(`Évaluation ${this.isEditing ? 'mise à jour' : 'créée'} avec succès !`);
        this.resetForm();
      },
      error: (err) => {
        console.error(`Erreur lors de la ${this.isEditing ? 'mise à jour' : 'création'} de l'évaluation:`, err);
      }
    });
  }
  
  // ✅ ================== MÉTHODE editEvaluation AJOUTÉE ==================
  editEvaluation(evaluation: Evaluation): void {
    this.isEditing = true;
    this.currentEvaluationId = evaluation.id!;

    this.evaluationForm.patchValue({
      dateEvaluation: this.datePipe.transform(evaluation.dateEvaluation, 'yyyy-MM-dd'),
      typeEvaluation: evaluation.typeEvaluation,
      duree: evaluation.duree,
      moduleId: evaluation.module?.id
    });

    window.scrollTo(0, 0);
  }

  deleteEvaluation(id: number) {
    this.evaluationService.deleteEvaluation(id).subscribe({
      next: () => {
        console.log('Évaluation supprimée avec succès !');
        this.refreshEvaluations();
      },
      error: (err) => console.error('Erreur lors de la suppression de l\'évaluation:', err)
    });
  }

  // ✅ ================== MÉTHODE resetForm AJOUTÉE ==================
  resetForm(): void {
    this.evaluationForm.reset();
    this.isEditing = false;
    this.currentEvaluationId = null;
    this.refreshEvaluations();
  }
}
