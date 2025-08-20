import { Utilisateur } from './utilisateur.model';
import { Module } from './module.model';

export interface Formation {
  id: number;
  titre: string;
  description: string;
  dateDebut: Date; 
  dateFin: Date; 
  formateur: Utilisateur;
  modules: Module[];
}


export interface FormationDTO {
  id?: number; 
  titre: string;
  description: string;
  formateurId: number; 
  moduleIds: number[]; 
  dateDebut: string;   
  dateFin: string;    
}
