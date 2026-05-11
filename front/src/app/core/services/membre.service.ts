import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Membre } from '../models/membre.model';
import { Emprunt } from '../models/emprunt.model';

@Injectable({ providedIn: 'root' })
export class MembreService {
  private http = inject(HttpClient);
  private url = '/api/membres';

  getAll() { return this.http.get<Membre[]>(this.url); }
  getById(id: number) { return this.http.get<Membre>(`${this.url}/${id}`); }
  create(m: Membre) { return this.http.post<Membre>(this.url, m); }
  update(id: number, m: Membre) { return this.http.put<Membre>(`${this.url}/${id}`, m); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
  getEmprunts(id: number) { return this.http.get<Emprunt[]>(`${this.url}/${id}/emprunts`); }
}
