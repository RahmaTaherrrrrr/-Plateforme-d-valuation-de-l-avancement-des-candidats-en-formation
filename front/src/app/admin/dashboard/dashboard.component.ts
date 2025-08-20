// ==================== COPIEZ ET REMPLACEZ VOTRE FICHIER PAR CECI ====================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from '../../shared/layout/layout.component';
import { UtilisateurService } from '../../services/utilisateur.service';
import { FormationService } from '../../services/formation.service';
import { ModuleService } from '../../services/module.service';
import { EvaluationService } from '../../services/evaluation.service';
import { Utilisateur } from '../../models/utilisateur.model';
import { Role } from '../../models/role.model';
import { Formation } from '../../models/formation.model';
import { Module } from '../../models/module.model';
import { Evaluation } from '../../models/evaluation.model';import { firstValueFrom } from 'rxjs'; // <-- **ÉTAPE 1 : L'IMPORT NÉCESSAIRE**

interface StatCard {
  title: string;
  value: number;
  icon: string;
  color: string;
  change: string;
  changeType: 'increase' | 'decrease';
}

interface ChartData {
  labels: string[];
  datasets: any[];
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  loading = true;
  stats: StatCard[] = [];
  recentUsers: Utilisateur[] = [];
  recentFormations: Formation[] = [];
  candidatsParGenre: ChartData = { labels: [], datasets: [] };
  evaluationsParMois: ChartData = { labels: [], datasets: [] };

  constructor(
    private utilisateurService: UtilisateurService,
    private formationService: FormationService,
    private moduleService: ModuleService,
    private evaluationService: EvaluationService
  ) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  async loadDashboardData() {
    try {
      this.loading = true;
      
      // **ÉTAPE 2 : LA CORRECTION AVEC firstValueFrom**
      // On remplace .toPromise() par la nouvelle syntaxe
      const [utilisateurs, formations, modules, evaluations] = await Promise.all([
        firstValueFrom(this.utilisateurService.getAllUtilisateurs()),
        firstValueFrom(this.formationService.getFormations()),
        firstValueFrom(this.moduleService.getAllModules()),
        firstValueFrom(this.evaluationService.getAllEvaluations())
      ]);

      // Les erreurs "en rouge" disparaissent ici car les types sont maintenant corrects
      this.calculateStats(utilisateurs, formations, modules, evaluations);
      this.prepareChartData(utilisateurs, evaluations);
      this.recentUsers = utilisateurs.slice(-5).reverse();
      this.recentFormations = formations.slice(-5).reverse();

    } catch (error) {
      console.error('Erreur lors du chargement des données:', error);
    } finally {
      this.loading = false;
    }
  }

 

  calculateStats(utilisateurs: Utilisateur[], formations: Formation[], modules: Module[], evaluations: Evaluation[]) {
    const totalUtilisateurs = utilisateurs.length;
    const totalFormateurs = utilisateurs.filter(u => u.role === Role.FORMATEUR).length;
    const totalCandidats = utilisateurs.filter(u => u.role === Role.CANDIDAT).length;
    const totalFormations = formations.length;
    const totalModules = modules.length;
    const totalEvaluations = evaluations.length;

    this.stats = [
      {
        title: 'Total Utilisateurs',
        value: totalUtilisateurs,
        icon: 'bi-people',
        color: 'primary',
        change: '+12%',
        changeType: 'increase'
      },
      {
        title: 'Formateurs',
        value: totalFormateurs,
        icon: 'bi-person-workspace',
        color: 'warning',
        change: '+5%',
        changeType: 'increase'
      },
      {
        title: 'Candidats',
        value: totalCandidats,
        icon: 'bi-person-check',
        color: 'success',
        change: '+18%',
        changeType: 'increase'
      },
      {
        title: 'Formations',
        value: totalFormations,
        icon: 'bi-book',
        color: 'info',
        change: '+8%',
        changeType: 'increase'
      },
      {
        title: 'Modules',
        value: totalModules,
        icon: 'bi-collection',
        color: 'secondary',
        change: '+15%',
        changeType: 'increase'
      },
      {
        title: 'Évaluations',
        value: totalEvaluations,
        icon: 'bi-clipboard-check',
        color: 'danger',
        change: '+22%',
        changeType: 'increase'
      }
    ];
  }

  prepareChartData(utilisateurs: Utilisateur[], evaluations: Evaluation[]) {
    // Données pour le graphique des candidats par genre (simulation)
    const candidats = utilisateurs.filter(u => u.role === Role.CANDIDAT);
    const hommes = Math.floor(candidats.length * 0.6); // 60% hommes (simulation)
    const femmes = candidats.length - hommes;

    this.candidatsParGenre = {
      labels: ['Hommes', 'Femmes'],
      datasets: [{
        data: [hommes, femmes],
        backgroundColor: ['#667eea', '#764ba2'],
        borderWidth: 0
      }]
    };

    // Données pour le graphique des évaluations par mois (simulation)
    const mois = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun'];
    const evaluationsParMois = mois.map(() => Math.floor(Math.random() * 50) + 10);

    this.evaluationsParMois = {
      labels: mois,
      datasets: [{
        label: 'Évaluations',
        data: evaluationsParMois,
        backgroundColor: 'rgba(102, 126, 234, 0.1)',
        borderColor: '#667eea',
        borderWidth: 2,
        fill: true
      }]
    };
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

  formatDate(date: Date | string): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('fr-FR');
  }
}
