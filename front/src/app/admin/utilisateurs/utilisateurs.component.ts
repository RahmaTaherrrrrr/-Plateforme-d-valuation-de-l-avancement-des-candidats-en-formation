import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LayoutComponent } from '../../shared/layout/layout.component';
import { UtilisateurService } from '../../services/utilisateur.service';
import { Utilisateur, Role } from '../../models/utilisateur.model';

@Component({
  selector: 'app-utilisateurs',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './utilisateurs.component.html',
  styleUrls: ['./utilisateurs.component.scss']
})
export class UtilisateursComponent implements OnInit {
  utilisateurs: Utilisateur[] = [];
  filteredUtilisateurs: Utilisateur[] = [];
  loading = true;
  showModal = false;
  editMode = false;
  selectedUtilisateur: Utilisateur | null = null;
  utilisateurForm: FormGroup;
  searchTerm = '';
  selectedRole = '';
  roles = Object.values(Role);

  constructor(
    private utilisateurService: UtilisateurService,
    private fb: FormBuilder
  ) {
    this.utilisateurForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      password: ['', [Validators.minLength(6)]],
      role: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.loadUtilisateurs();
  }

  loadUtilisateurs() {
    this.loading = true;
    this.utilisateurService.getAllUtilisateurs().subscribe({
      next: (data) => {
        this.utilisateurs = data;
        this.filteredUtilisateurs = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des utilisateurs:', error);
        this.loading = false;
      }
    });
  }

  filterUtilisateurs() {
    this.filteredUtilisateurs = this.utilisateurs.filter(user => {
      const matchesSearch = !this.searchTerm || 
        user.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.prenom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesRole = !this.selectedRole || user.role === this.selectedRole;
      
      return matchesSearch && matchesRole;
    });
  }

  openModal(utilisateur?: Utilisateur) {
    this.editMode = !!utilisateur;
    this.selectedUtilisateur = utilisateur || null;
    
    if (this.editMode && utilisateur) {
      this.utilisateurForm.patchValue({
        nom: utilisateur.nom,
        prenom: utilisateur.prenom,
        email: utilisateur.email,
        telephone: utilisateur.telephone,
        role: utilisateur.role
      });
      // Rendre le mot de passe optionnel en mode édition
      this.utilisateurForm.get('password')?.clearValidators();
    } else {
      this.utilisateurForm.reset();
      // Rendre le mot de passe obligatoire en mode création
      this.utilisateurForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
    
    this.utilisateurForm.get('password')?.updateValueAndValidity();
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.editMode = false;
    this.selectedUtilisateur = null;
    this.utilisateurForm.reset();
  }

  onSubmit() {
    if (this.utilisateurForm.valid) {
      const formData = this.utilisateurForm.value;
      
      if (this.editMode && this.selectedUtilisateur) {
        // Mode édition
        const updatedUser: Utilisateur = {
          ...this.selectedUtilisateur,
          ...formData
        };
        
        // Ne pas inclure le mot de passe s'il est vide
        if (!formData.password) {
          delete updatedUser.password;
        }
        
        this.utilisateurService.updateUtilisateur(this.selectedUtilisateur.id!, updatedUser).subscribe({
          next: () => {
            this.loadUtilisateurs();
            this.closeModal();
          },
          error: (error) => {
            console.error('Erreur lors de la mise à jour:', error);
          }
        });
      } else {
        // Mode création
        this.utilisateurService.createUtilisateur(formData).subscribe({
          next: () => {
            this.loadUtilisateurs();
            this.closeModal();
          },
          error: (error) => {
            console.error('Erreur lors de la création:', error);
          }
        });
      }
    }
  }

  deleteUtilisateur(utilisateur: Utilisateur) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer ${utilisateur.prenom} ${utilisateur.nom} ?`)) {
      this.utilisateurService.deleteUtilisateur(utilisateur.id!).subscribe({
        next: () => {
          this.loadUtilisateurs();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
        }
      });
    }
  }

  getRoleDisplayName(role: Role): string {
    switch (role) {
      case Role.ADMIN:
        return 'Administrateur';
      case Role.FORMATEUR:
        return 'Formateur';
      case Role.CANDIDAT:
        return 'Candidat';
      default:
        return role;
    }
  }

  getRoleBadgeClass(role: Role): string {
    switch (role) {
      case Role.ADMIN:
        return 'bg-danger';
      case Role.FORMATEUR:
        return 'bg-warning';
      case Role.CANDIDAT:
        return 'bg-success';
      default:
        return 'bg-secondary';
    }
  }

  // Getters pour faciliter l'accès aux contrôles du formulaire
  get nom() { return this.utilisateurForm.get('nom'); }
  get prenom() { return this.utilisateurForm.get('prenom'); }
  get email() { return this.utilisateurForm.get('email'); }
  get telephone() { return this.utilisateurForm.get('telephone'); }
  get password() { return this.utilisateurForm.get('password'); }
  get role() { return this.utilisateurForm.get('role'); }
}

