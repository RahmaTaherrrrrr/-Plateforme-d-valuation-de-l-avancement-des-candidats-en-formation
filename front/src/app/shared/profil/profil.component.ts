import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { LayoutComponent } from '../layout/layout.component';
import { AuthService } from '../../services/auth.service';
import { UtilisateurService } from '../../services/utilisateur.service';
import { Utilisateur, Role } from '../../models/utilisateur.model';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LayoutComponent],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {
  currentUser: Utilisateur | null = null;
  profilForm: FormGroup;
  passwordForm: FormGroup;
  loading = false;
  showPasswordForm = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private utilisateurService: UtilisateurService
  ) {
    this.profilForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['']
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.profilForm.patchValue({
          nom: user.nom,
          prenom: user.prenom,
          email: user.email,
          telephone: user.telephone
        });
      }
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword');
    const confirmPassword = form.get('confirmPassword');
    
    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
    } else {
      if (confirmPassword?.errors?.['passwordMismatch']) {
        delete confirmPassword.errors['passwordMismatch'];
        if (Object.keys(confirmPassword.errors).length === 0) {
          confirmPassword.setErrors(null);
        }
      }
    }
    return null;
  }

  onSubmitProfil() {
    if (this.profilForm.valid && this.currentUser) {
      this.loading = true;
      this.errorMessage = '';
      
      const updatedUser: Utilisateur = {
        ...this.currentUser,
        ...this.profilForm.value
      };

      this.utilisateurService.updateProfil(updatedUser).subscribe({
        next: (response) => {
          this.loading = false;
          this.successMessage = 'Profil mis à jour avec succès';
          // Mettre à jour l'utilisateur dans le service d'authentification
          localStorage.setItem('currentUser', JSON.stringify(response));
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = 'Erreur lors de la mise à jour du profil';
          console.error('Erreur:', error);
        }
      });
    }
  }

  onSubmitPassword() {
    if (this.passwordForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      // Ici vous devriez appeler un service pour changer le mot de passe
      // Pour l'instant, on simule une réponse
      setTimeout(() => {
        this.loading = false;
        this.successMessage = 'Mot de passe modifié avec succès';
        this.passwordForm.reset();
        this.showPasswordForm = false;
        setTimeout(() => this.successMessage = '', 3000);
      }, 1000);
    }
  }

  togglePasswordForm() {
    this.showPasswordForm = !this.showPasswordForm;
    if (!this.showPasswordForm) {
      this.passwordForm.reset();
    }
  }

  getRoleDisplayName(): string {
    if (!this.currentUser) return '';
    switch (this.currentUser.role) {
      case Role.ADMIN:
        return 'Administrateur';
      case Role.FORMATEUR:
        return 'Formateur';
      case Role.CANDIDAT:
        return 'Candidat';
      default:
        return this.currentUser.role;
    }
  }

  getRoleColor(): string {
    if (!this.currentUser) return 'secondary';
    switch (this.currentUser.role) {
      case Role.ADMIN:
        return 'danger';
      case Role.FORMATEUR:
        return 'warning';
      case Role.CANDIDAT:
        return 'success';
      default:
        return 'secondary';
    }
  }

  // Getters pour faciliter l'accès aux contrôles du formulaire
  get nom() { return this.profilForm.get('nom'); }
  get prenom() { return this.profilForm.get('prenom'); }
  get email() { return this.profilForm.get('email'); }
  get telephone() { return this.profilForm.get('telephone'); }
  get currentPassword() { return this.passwordForm.get('currentPassword'); }
  get newPassword() { return this.passwordForm.get('newPassword'); }
  get confirmPassword() { return this.passwordForm.get('confirmPassword'); }
}

