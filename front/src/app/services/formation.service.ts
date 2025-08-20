import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Formation, FormationDTO } from '../models/formation.model';

@Injectable({
  providedIn: 'root'
} )
export class FormationService {
  private apiUrl = 'http://localhost:8083/api/formations';

  constructor(private http: HttpClient ) {}

  getFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(this.apiUrl );
  }


  createFormation(formation: FormationDTO): Observable<Formation> {
    return this.http.post<Formation>(this.apiUrl, formation );
  }

  updateFormation(id: number, formation: FormationDTO): Observable<Formation> {
    return this.http.put<Formation>(`${this.apiUrl}/${id}`, formation );
  }

  deleteFormation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}` );
  }
}
