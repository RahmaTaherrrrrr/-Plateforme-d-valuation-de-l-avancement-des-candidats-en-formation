
import { Routes } from '@angular/router';


import { LayoutComponent } from './shared/layout/layout.component'; 

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent) },
  { path: 'unauthorized', loadComponent: () => import('./shared/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent) },

  {
    path: '', 
    component: LayoutComponent,
    children: [
      {
        path: 'admin',
        data: { expectedRole: 'ADMIN' },
        children: [
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
          { path: 'dashboard', loadComponent: () => import('./admin/dashboard/dashboard.component').then(m => m.DashboardComponent) },
          { path: 'utilisateurs', loadComponent: () => import('./admin/utilisateurs/utilisateurs.component').then(m => m.UtilisateursComponent) },
          { path: 'formations', loadComponent: () => import('./admin/formations/formations.component').then(m => m.FormationsComponent) },
          { path: 'modules', loadComponent: () => import('./admin/modules/modules.component').then(m => m.ModulesComponent) },
          { path: 'evaluations', loadComponent: () => import('./admin/evaluations/evaluations.component').then(m => m.EvaluationsComponent) },
          { path: 'profil', loadComponent: () => import('./shared/profil/profil.component').then(m => m.ProfilComponent) }
        ]
      },
      {
        path: 'formateur',
        data: { expectedRole: 'FORMATEUR' },
        children: [
        ]
      },
      {
        path: 'candidat',
        data: { expectedRole: 'CANDIDAT' },
        children: [
        ]
      }
    ]
  },

  { path: '**', redirectTo: '/login' }
];
