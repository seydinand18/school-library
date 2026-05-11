import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MembreService } from '../../core/services/membre.service';
import { LoaderService } from '../../core/services/loader.service';
import { Membre } from '../../core/models/membre.model';

@Component({
  selector: 'app-membre-form',
  standalone: true,
  imports: [FormsModule, InputTextModule, InputNumberModule, SelectModule, ButtonModule, ToastModule, RouterLink],
  providers: [MessageService],
  templateUrl: './membre-form.component.html'
})
export class MembreFormComponent implements OnInit {
  router = inject(Router);
  private route = inject(ActivatedRoute);
  private membreService = inject(MembreService);
  private messageService = inject(MessageService);
  private loader = inject(LoaderService);

  isEdit = signal(false);
  saving = signal(false);
  roles = ['ELEVE', 'PROFESSEUR'];
  membre: Membre = { nom: '', prenom: '', email: '', role: 'ELEVE', quota: 3 };
  private id?: number;

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.isEdit.set(true);
      this.loader.show();
      this.membreService.getById(this.id).subscribe({ next: m => { this.membre = m; this.loader.hide(); }, error: () => this.loader.hide() });
    }
  }

  sauvegarder() {
    this.saving.set(true);
    const op = this.isEdit() ? this.membreService.update(this.id!, this.membre) : this.membreService.create(this.membre);
    op.subscribe({
      next: () => this.router.navigate(['/membres']),
      error: () => { this.saving.set(false); this.messageService.add({ severity: 'error', summary: 'Erreur' }); }
    });
  }
}
