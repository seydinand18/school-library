import { Component, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { EmpruntService } from '../../core/services/emprunt.service';
import { MembreService } from '../../core/services/membre.service';
import { LivreService } from '../../core/services/livre.service';
import { LoaderService } from '../../core/services/loader.service';

@Component({
  selector: 'app-emprunt-form',
  standalone: true,
  imports: [FormsModule, SelectModule, ButtonModule, ToastModule, RouterLink],
  providers: [MessageService],
  templateUrl: './emprunt-form.component.html'
})
export class EmpruntFormComponent implements OnInit {
  router = inject(Router);
  private empruntService = inject(EmpruntService);
  private membreService = inject(MembreService);
  private livreService = inject(LivreService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  membres = signal<{ label: string; value: number }[]>([]);
  livres = signal<{ label: string; value: number }[]>([]);
  saving = signal(false);
  membreId?: number;
  livreId?: number;

  ngOnInit() {
    this.loader.show();
    this.membreService.getAll().subscribe(data => {
      this.membres.set(data.map(m => ({ label: `${m.prenom} ${m.nom}`, value: m.id! })));
    });
    this.livreService.getAll().subscribe(data => {
      this.livres.set(data.filter(l => (l.quantiteDisponible ?? 0) > 0)
        .map(l => ({ label: `${l.titre}  (${l.quantiteDisponible} dispo)`, value: l.id! })));
      this.loader.hide();
    });
  }

  sauvegarder() {
    this.saving.set(true);
    this.empruntService.create({ membreId: this.membreId!, livreId: this.livreId! }).subscribe({
      next: () => this.router.navigate(['/emprunts']),
      error: () => { this.saving.set(false); this.messageService.add({ severity: 'error', summary: 'Erreur lors de la création' }); }
    });
  }
}
