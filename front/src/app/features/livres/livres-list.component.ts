import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { LivreService } from '../../core/services/livre.service';
import { LoaderService } from '../../core/services/loader.service';
import { Livre } from '../../core/models/livre.model';

@Component({
  selector: 'app-livres-list',
  standalone: true,
  imports: [TableModule, ButtonModule, TagModule, RouterLink, ConfirmDialogModule, ToastModule, InputTextModule, FormsModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './livres-list.component.html'
})
export class LivresListComponent implements OnInit {
  private livreService = inject(LivreService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  livres = signal<Livre[]>([]);
  livresFiltres = signal<Livre[]>([]);
  recherche = '';

  ngOnInit() { this.charger(); }

  charger() {
    this.loader.show();
    this.livreService.getAll().subscribe({
      next: data => { this.livres.set(data); this.livresFiltres.set(data); this.loader.hide(); },
      error: () => this.loader.hide()
    });
  }

  filtrer() {
    const q = this.recherche.toLowerCase();
    this.livresFiltres.set(q
      ? this.livres().filter(l => `${l.titre} ${l.auteur} ${l.isbn}`.toLowerCase().includes(q))
      : this.livres()
    );
  }

  disponibiliteTag(livre: Livre): 'success' | 'danger' {
    return livre.quantiteDisponible! > 0 ? 'success' : 'danger';
  }

  confirmerSuppression(livre: Livre) {
    this.confirmationService.confirm({
      message: `Supprimer "${livre.titre}" ?`,
      header: 'Confirmation',
      icon: 'pi pi-trash',
      accept: () => {
        this.livreService.delete(livre.id!).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Livre supprimé' }); this.charger(); }
        });
      }
    });
  }
}
