import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Utilisateur } from '../models/utilisateur.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {
  private apiUrl = 'http://localhost:8083/api/utilisateurs';

  constructor(private http: HttpClient, private authService: AuthService) {
    console.log('UtilisateurService initialisé');
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    console.log('Token récupéré pour getAuthHeaders :', token); 
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }


getFormateurs(): Observable<Utilisateur[]> {
  return this.http.get<Utilisateur[]>(`${this.apiUrl}/formateurs`, { headers: this.getAuthHeaders() })
    .pipe(
      tap(response => console.log('Réponse getFormateurs:', response)),
      catchError(err => {
        console.error('Erreur dans getFormateurs:', err);
        return throwError(() => new Error(err.message || 'Erreur chargement formateurs'));
      })
    );
}


  getAllUtilisateurs(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(this.apiUrl, { headers: this.getAuthHeaders() })
      .pipe(
        tap(response => console.log('Réponse de getAllUtilisateurs :', response)),
        catchError(err => {
          console.error('Erreur dans getAllUtilisateurs :', err);
          return throwError(() => new Error(err.message || 'Échec du chargement des utilisateurs'));
        })
      );
  }

  getUtilisateurById(id: number): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans getUtilisateurById :', err);
          return throwError(() => new Error(err.message || 'Échec du chargement de l\'utilisateur'));
        })
      );
  }

  createUtilisateur(utilisateur: Utilisateur): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(this.apiUrl, utilisateur, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans createUtilisateur :', err);
          return throwError(() => new Error(err.message || 'Échec de la création de l\'utilisateur'));
        })
      );
  }

  updateUtilisateur(id: number, utilisateur: Utilisateur): Observable<Utilisateur> {
    return this.http.put<Utilisateur>(`${this.apiUrl}/${id}`, utilisateur, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans updateUtilisateur :', err);
          return throwError(() => new Error(err.message || 'Échec de la mise à jour de l\'utilisateur'));
        })
      );
  }

  deleteUtilisateur(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans deleteUtilisateur :', err);
          return throwError(() => new Error(err.message || 'Échec de la suppression de l\'utilisateur'));
        })
      );
  }

  getCandidats(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.apiUrl}/candidats`, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans getCandidats :', err);
          return throwError(() => new Error(err.message || 'Échec du chargement des candidats'));
        })
      );
  }

  updateProfil(utilisateur: Utilisateur): Observable<Utilisateur> {
    return this.http.put<Utilisateur>(`${this.apiUrl}/profil`, utilisateur, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(err => {
          console.error('Erreur dans updateProfil :', err);
          return throwError(() => new Error(err.message || 'Échec de la mise à jour du profil'));
        })
      );
  }
 
}