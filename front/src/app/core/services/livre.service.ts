import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Livre } from '../models/livre.model';

@Injectable({ providedIn: 'root' })
export class LivreService {
  private http = inject(HttpClient);
  private url = '/api/livres';

  getAll() { return this.http.get<Livre[]>(this.url); }
  getById(id: number) { return this.http.get<Livre>(`${this.url}/${id}`); }
  create(l: Livre) { return this.http.post<Livre>(this.url, l); }
  update(id: number, l: Livre) { return this.http.put<Livre>(`${this.url}/${id}`, l); }
  delete(id: number) { return this.http.delete<void>(`${this.url}/${id}`); }
}
