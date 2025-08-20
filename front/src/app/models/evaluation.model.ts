import { Module } from './module.model';

export interface Evaluation {
  id: number;
  dateEvaluation: Date;
  typeEvaluation: string;
  duree: number;
  module?: Module;
}