import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Emprunt } from '../models/emprunt.model';

@Injectable({ providedIn: 'root' })
export class EmpruntService {
  private http = inject(HttpClient);
  private url = '/api/emprunts';

  getAll() { return this.http.get<Emprunt[]>(this.url); }
  getById(id: number) { return this.http.get<Emprunt>(`${this.url}/${id}`); }
  getByMembre(membreId: number) { return this.http.get<Emprunt[]>(`${this.url}/membre/${membreId}`); }
  create(e: { membreId: number; livreId: number }) { return this.http.post<Emprunt>(this.url, e); }
  retourner(id: number) { return this.http.patch<Emprunt>(`${this.url}/${id}/retour`, {}); }
}
