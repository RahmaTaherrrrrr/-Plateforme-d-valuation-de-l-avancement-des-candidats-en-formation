
export interface LoginRequest {
  email: string;
  password: string;
}


export interface LoginResponse {
  token: string;
  utilisateur: Utilisateur;
}


export interface RegisterRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  telephone?: string; 
  role: Role;
}


export enum Role {
  ADMIN = 'ADMIN',
  FORMATEUR = 'FORMATEUR',
  CANDIDAT = 'CANDIDAT'
}


export interface Utilisateur {
  id?: number; 
  nom: string;
  prenom: string;
  email: string;
  password?: string; 
  telephone?: string;
  role: Role;
}



