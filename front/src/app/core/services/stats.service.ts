import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Stats {
  livres:   { total: number; disponibles: number; empruntes: number };
  membres:  { total: number; eleves: number; professeurs: number };
  emprunts: { en_cours: number; en_retard: number; retournes: number };
}

@Injectable({ providedIn: 'root' })
export class StatsService {
  private http = inject(HttpClient);
  get() { return this.http.get<Stats>('/api/stats'); }
}
