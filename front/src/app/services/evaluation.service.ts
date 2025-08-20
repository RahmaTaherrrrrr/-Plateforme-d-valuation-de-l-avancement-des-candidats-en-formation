
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Evaluation } from '../models/evaluation.model';
import { AuthService } from './auth.service'; 

@Injectable({
  providedIn: 'root'
} )
export class EvaluationService {
  private apiUrl = 'http://localhost:8083/api/evaluations'; 

  constructor(private http: HttpClient, private authService: AuthService ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getAllEvaluations(): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(this.apiUrl, { headers: this.getAuthHeaders( ) });
  }

  createEvaluation(evaluation: any): Observable<Evaluation> {
    return this.http.post<Evaluation>(this.apiUrl, evaluation, { headers: this.getAuthHeaders( ) });
  }

  deleteEvaluation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders( ) });
  }

  /**
   * Met à jour une évaluation existante sur le serveur.
   * @param id L'ID de l'évaluation à mettre à jour.
   * @param evaluationPayload Les nouvelles données de l'évaluation.
   * @returns Un Observable de l'évaluation mise à jour.
   */
  updateEvaluation(id: number, evaluationPayload: any): Observable<Evaluation> {
    const url = `${this.apiUrl}/${id}`;
    
    return this.http.put<Evaluation>(url, evaluationPayload, { headers: this.getAuthHeaders( ) });
  }
}
