import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { AvatarModule } from 'primeng/avatar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { MembreService } from '../../core/services/membre.service';
import { LoaderService } from '../../core/services/loader.service';
import { Membre } from '../../core/models/membre.model';

@Component({
  selector: 'app-membres-list',
  standalone: true,
  imports: [TableModule, ButtonModule, TagModule, AvatarModule, RouterLink, ConfirmDialogModule, ToastModule, InputTextModule, FormsModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './membres-list.component.html'
})
export class MembresListComponent implements OnInit {
  private membreService = inject(MembreService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  membres = signal<Membre[]>([]);
  membresFiltres = signal<Membre[]>([]);
  recherche = '';

  ngOnInit() { this.charger(); }

  charger() {
    this.loader.show();
    this.membreService.getAll().subscribe({
      next: data => { this.membres.set(data); this.membresFiltres.set(data); this.loader.hide(); },
      error: () => this.loader.hide()
    });
  }

  filtrer() {
    const q = this.recherche.toLowerCase();
    this.membresFiltres.set(q
      ? this.membres().filter(m => `${m.nom} ${m.prenom} ${m.email}`.toLowerCase().includes(q))
      : this.membres()
    );
  }

  roleSeverity(role: string): 'secondary' | 'success' {
    return role === 'PROFESSEUR' ? 'secondary' : 'success';
  }

  avatarLabel(m: Membre): string {
    return `${m.prenom[0]}${m.nom[0]}`;
  }

  confirmerSuppression(m: Membre) {
    this.confirmationService.confirm({
      message: `Supprimer ${m.prenom} ${m.nom} ?`,
      header: 'Confirmation',
      icon: 'pi pi-user-minus',
      accept: () => {
        this.membreService.delete(m.id!).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Membre supprimé' }); this.charger(); }
        });
      }
    });
  }
}
