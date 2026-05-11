import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'livres', pathMatch: 'full' },
  {
    path: 'livres',
    loadComponent: () => import('./features/livres/livres-list.component').then(m => m.LivresListComponent)
  },
  {
    path: 'livres/new',
    loadComponent: () => import('./features/livres/livre-form.component').then(m => m.LivreFormComponent)
  },
  {
    path: 'livres/:id/edit',
    loadComponent: () => import('./features/livres/livre-form.component').then(m => m.LivreFormComponent)
  },
  {
    path: 'membres',
    loadComponent: () => import('./features/membres/membres-list.component').then(m => m.MembresListComponent)
  },
  {
    path: 'membres/new',
    loadComponent: () => import('./features/membres/membre-form.component').then(m => m.MembreFormComponent)
  },
  {
    path: 'membres/:id',
    loadComponent: () => import('./features/membres/membre-detail.component').then(m => m.MembreDetailComponent)
  },
  {
    path: 'membres/:id/edit',
    loadComponent: () => import('./features/membres/membre-form.component').then(m => m.MembreFormComponent)
  },
  {
    path: 'emprunts',
    loadComponent: () => import('./features/emprunts/emprunts-list.component').then(m => m.EmpruntsListComponent)
  },
  {
    path: 'emprunts/new',
    loadComponent: () => import('./features/emprunts/emprunt-form.component').then(m => m.EmpruntFormComponent)
  },
  {
    path: 'chat',
    loadComponent: () => import('./features/chatbot/chatbot.component').then(m => m.ChatbotComponent)
  },
  {
    path: 'stats',
    loadComponent: () => import('./features/stats/stats.component').then(m => m.StatsComponent)
  }
];
