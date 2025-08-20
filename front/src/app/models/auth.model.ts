import { Utilisateur } from './utilisateur.model';
import { Role } from './role.model';

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
  telephone?: string;
  password: string;
  role: Role;
}