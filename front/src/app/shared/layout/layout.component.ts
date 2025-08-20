import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Utilisateur, Role } from '../../models/utilisateur.model';
import { filter } from 'rxjs';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  currentUser: Utilisateur | null = null;
  sidebarCollapsed = false;
  currentRoute = '';
  dropdownOpen = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    // Écouter les changements de route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.currentRoute = event.url;
    });
  }

  toggleSidebar() {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  getMenuItems() {
    if (!this.currentUser) return [];

    const baseItems = [
      { icon: 'bi-person-circle', label: 'Profil', route: 'profil' }
    ];

    switch (this.currentUser.role) {
      case Role.ADMIN:
        return [
          { icon: 'bi-speedometer2', label: 'Dashboard', route: '/admin/dashboard' },
          { icon: 'bi-people', label: 'Utilisateurs', route: '/admin/utilisateurs' },
          { icon: 'bi-book', label: 'Formations', route: '/admin/formations' },
          { icon: 'bi-collection', label: 'Modules', route: '/admin/modules' },
          { icon: 'bi-clipboard-check', label: 'Évaluations', route: '/admin/evaluations' },
          ...baseItems.map(item => ({ ...item, route: '/admin/' + item.route }))
        ];
      case Role.FORMATEUR:
        return [
          { icon: 'bi-speedometer2', label: 'Dashboard', route: '/formateur/dashboard' },
          { icon: 'bi-book', label: 'Mes Formations', route: '/formateur/formations' },
          { icon: 'bi-people', label: 'Candidats', route: '/formateur/candidats' },
          { icon: 'bi-clipboard-check', label: 'Évaluations', route: '/formateur/evaluations' },
          ...baseItems.map(item => ({ ...item, route: '/formateur/' + item.route }))
        ];
      case Role.CANDIDAT:
        return [
          { icon: 'bi-speedometer2', label: 'Dashboard', route: '/candidat/dashboard' },
          { icon: 'bi-book', label: 'Formations', route: '/candidat/formations' },
          { icon: 'bi-collection', label: 'Modules', route: '/candidat/modules' },
          { icon: 'bi-clipboard-check', label: 'Évaluations', route: '/candidat/evaluations' },
          { icon: 'bi-graph-up', label: 'Progression', route: '/candidat/progression' },
          ...baseItems.map(item => ({ ...item, route: '/candidat/' + item.route }))
        ];
      default:
        return baseItems;
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

  isActiveRoute(route: string): boolean {
    return this.currentRoute.includes(route);
  }
}

