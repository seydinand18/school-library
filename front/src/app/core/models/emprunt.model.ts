import { Membre } from './membre.model';
import { Livre } from './livre.model';

export interface Emprunt {
  id?: number;
  membreId: number;
  livreId: number;
  dateEmprunt?: string;
  dateRetourPrevue?: string;
  dateRetourEffective?: string;
  statut?: 'EN_COURS' | 'RETOURNE' | 'EN_RETARD';
  membre?: Membre;
  livre?: Livre;
}
