export interface Membre {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  role: 'ELEVE' | 'PROFESSEUR';
  quota: number;
  dateInscription?: string;
}
