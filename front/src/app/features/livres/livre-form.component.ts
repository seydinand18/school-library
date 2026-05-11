import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { LivreService } from '../../core/services/livre.service';
import { LoaderService } from '../../core/services/loader.service';
import { Livre } from '../../core/models/livre.model';

@Component({
  selector: 'app-livre-form',
  standalone: true,
  imports: [FormsModule, InputTextModule, InputNumberModule, ButtonModule, ToastModule, RouterLink],
  providers: [MessageService],
  templateUrl: './livre-form.component.html'
})
export class LivreFormComponent implements OnInit {
  router = inject(Router);
  private route = inject(ActivatedRoute);
  private livreService = inject(LivreService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  isEdit = signal(false);
  saving = signal(false);
  livre: Livre = { titre: '', auteur: '', isbn: '', genre: '', classeScolaire: '', quantiteTotale: 1 };
  private id?: number;

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.isEdit.set(true);
      this.loader.show();
      this.livreService.getById(this.id).subscribe({ next: l => { this.livre = l; this.loader.hide(); }, error: () => this.loader.hide() });
    }
  }

  sauvegarder() {
    this.saving.set(true);
    const op = this.isEdit() ? this.livreService.update(this.id!, this.livre) : this.livreService.create(this.livre);
    op.subscribe({
      next: () => this.router.navigate(['/livres']),
      error: () => { this.saving.set(false); this.messageService.add({ severity: 'error', summary: 'Erreur lors de la sauvegarde' }); }
    });
  }
}
