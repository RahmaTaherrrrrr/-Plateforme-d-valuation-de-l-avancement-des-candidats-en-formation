import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest, Role } from '../../models/utilisateur.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  loading = false;
  error = '';
  success = false;
  roles = Object.values(Role);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      role: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
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

  onSubmit() {
    if (this.registerForm.valid) {
      this.loading = true;
      this.error = '';
      
      const userData: RegisterRequest = {
        nom: this.registerForm.value.nom,
        prenom: this.registerForm.value.prenom,
        email: this.registerForm.value.email,
        telephone: this.registerForm.value.telephone,
        password: this.registerForm.value.password,
        role: this.registerForm.value.role
      };
      
      this.authService.register(userData).subscribe({
        next: (response) => {
          this.loading = false;
          this.success = true;
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (error) => {
          this.loading = false;
          this.error = 'Erreur lors de la création du compte. Veuillez réessayer.';
          console.error('Erreur d\'inscription:', error);
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

  getRoleIcon(role: Role): string {
    switch (role) {
      case Role.ADMIN:
        return 'bi-shield-check';
      case Role.FORMATEUR:
        return 'bi-person-workspace';
      case Role.CANDIDAT:
        return 'bi-person-check';
      default:
        return 'bi-person';
    }
  }

  // Getters pour faciliter l'accès aux contrôles du formulaire
  get nom() { return this.registerForm.get('nom'); }
  get prenom() { return this.registerForm.get('prenom'); }
  get email() { return this.registerForm.get('email'); }
  get telephone() { return this.registerForm.get('telephone'); }
  get password() { return this.registerForm.get('password'); }
  get confirmPassword() { return this.registerForm.get('confirmPassword'); }
  get role() { return this.registerForm.get('role'); }
}

