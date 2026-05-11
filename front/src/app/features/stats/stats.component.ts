import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Stats, StatsService } from '../../core/services/stats.service';

@Component({
  selector: 'app-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stats.component.html'
})
export class StatsComponent implements OnInit {
  private svc = inject(StatsService);
  stats: Stats | null = null;
  loading = true;
  error = false;

  ngOnInit() {
    this.svc.get().subscribe({
      next: s  => { this.stats = s; this.loading = false; },
      error: () => { this.error = true; this.loading = false; }
    });
  }
}
