import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Formation } from '../../models/formation.model';

@Injectable({
  providedIn: 'root' 
})
export class FormationService {
  private apiUrl = 'http://localhost:8083/api/dashboard';

  constructor(private http: HttpClient) {}

  getFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(this.apiUrl);
  }
}
