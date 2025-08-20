import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest, Role } from '../../models/utilisateur.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      this.error = '';
      
      const credentials: LoginRequest = this.loginForm.value;
      
      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.loading = false;
          // Rediriger selon le rôle
          const user = response.utilisateur;
          switch (user.role) {
            case Role.ADMIN:
              this.router.navigate(['/admin/dashboard']);
              break;
            case Role.FORMATEUR:
              this.router.navigate(['/formateur/dashboard']);
              break;
            case Role.CANDIDAT:
              this.router.navigate(['/candidat/dashboard']);
              break;
            default:
              this.router.navigate(['/']);
          }
        },
        error: (error) => {
          this.loading = false;
          this.error = 'Email ou mot de passe incorrect';
          console.error('Erreur de connexion:', error);
        }
      });
    }
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }
}

