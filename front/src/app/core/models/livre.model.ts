export interface Livre {
  id?: number;
  titre: string;
  auteur: string;
  isbn: string;
  genre: string;
  classeScolaire: string;
  quantiteTotale: number;
  quantiteDisponible?: number;
}
