
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Module } from '../models/module.model'; 
import { AuthService } from './auth.service'; 
@Injectable({
  providedIn: 'root'
} )
export class ModuleService {
  private apiUrl = 'http://localhost:8083/api/modules'; 
  constructor(private http: HttpClient, private authService: AuthService ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
  getAllModules(): Observable<Module[]> {
    return this.http.get<Module[]>(this.apiUrl);
  }
    getModulesByFormationId(formationId: number): Observable<Module[]> {

    const url = `${this.apiUrl}/by-formation/${formationId}`;
    return this.http.get<Module[]>(url, { headers: this.getAuthHeaders( ) });
  }
  createModule(module: Partial<Module>): Observable<Module> {
    return this.http.post<Module>(this.apiUrl, module, { headers: this.getAuthHeaders( ) });
  }
  
  updateModule(id: number, module: Partial<Module>): Observable<Module> {
    return this.http.put<Module>(`${this.apiUrl}/${id}`, module, { headers: this.getAuthHeaders( ) });
  }

  deleteModule(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders( ) });
  }
  
}