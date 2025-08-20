import { Formation } from './formation.model';
import { Evaluation } from './evaluation.model';

export interface Module {
  id?: number;
  titre: string;
  description: string;
  duree: number;
  formation?: Formation;
  evaluations?: Evaluation[];
}