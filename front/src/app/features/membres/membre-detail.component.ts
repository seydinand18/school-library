import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AvatarModule } from 'primeng/avatar';
import { MembreService } from '../../core/services/membre.service';
import { LoaderService } from '../../core/services/loader.service';
import { Membre } from '../../core/models/membre.model';
import { Emprunt } from '../../core/models/emprunt.model';

@Component({
  selector: 'app-membre-detail',
  standalone: true,
  imports: [ButtonModule, TableModule, TagModule, AvatarModule, RouterLink],
  templateUrl: './membre-detail.component.html'
})
export class MembreDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private membreService = inject(MembreService);
  private loader = inject(LoaderService);

  membre = signal<Membre | null>(null);
  emprunts = signal<Emprunt[]>([]);

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];
    this.loader.show();
    this.membreService.getById(id).subscribe({ next: m => { this.membre.set(m); this.loader.hide(); }, error: () => this.loader.hide() });
    this.membreService.getEmprunts(id).subscribe(e => this.emprunts.set(e));
  }

  avatarLabel(m: Membre): string { return `${m.prenom[0]}${m.nom[0]}`; }

  statutSeverity(statut?: string): 'success' | 'danger' | 'info' {
    return statut === 'RETOURNE' ? 'success' : statut === 'EN_RETARD' ? 'danger' : 'info';
  }
}
