import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { EmpruntService } from '../../core/services/emprunt.service';
import { LoaderService } from '../../core/services/loader.service';
import { Emprunt } from '../../core/models/emprunt.model';

@Component({
  selector: 'app-emprunts-list',
  standalone: true,
  imports: [TableModule, ButtonModule, TagModule, RouterLink, ConfirmDialogModule, ToastModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './emprunts-list.component.html'
})
export class EmpruntsListComponent implements OnInit {
  private empruntService = inject(EmpruntService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  emprunts = signal<Emprunt[]>([]);

  enCours = () => this.emprunts().filter(e => e.statut === 'EN_COURS').length;
  enRetard = () => this.emprunts().filter(e => e.statut === 'EN_RETARD').length;

  ngOnInit() { this.charger(); }

  charger() {
    this.loader.show();
    this.empruntService.getAll().subscribe({
      next: data => { this.emprunts.set(data); this.loader.hide(); },
      error: () => this.loader.hide()
    });
  }

  statutSeverity(statut?: string): 'success' | 'danger' | 'info' {
    return statut === 'RETOURNE' ? 'success' : statut === 'EN_RETARD' ? 'danger' : 'info';
  }

  confirmerRetour(e: Emprunt) {
    this.confirmationService.confirm({
      message: 'Confirmer le retour de ce livre ?',
      header: 'Enregistrer un retour',
      icon: 'pi pi-undo',
      accept: () => {
        this.empruntService.retourner(e.id!).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Retour enregistré !' }); this.charger(); }
        });
      }
    });
  }
}
