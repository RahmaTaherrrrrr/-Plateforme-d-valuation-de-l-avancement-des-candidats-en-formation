
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, Utilisateur } from '../models/utilisateur.model';

@Injectable({
  providedIn: 'root'
} )
export class AuthService {
  private apiUrl = 'http://localhost:8083/api/auth';

  private currentUserSubject = new BehaviorSubject<Utilisateur | null>(null );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient ) {
 
    this.loadUserFromStorage();
  }

  
  private loadUserFromStorage(): void {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials ).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('currentUser', JSON.stringify(response.utilisateur));

        this.currentUserSubject.next(response.utilisateur);
      })
    );
  }

  
  register(userData: RegisterRequest): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(`${this.apiUrl}/register`, userData );
  }


  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }


  getToken(): string | null {
    return localStorage.getItem('token');
  }

 
  getCurrentUser(): Utilisateur | null {
    return this.currentUserSubject.value;
  }


  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user ? user.role === role : false;
  }
}
