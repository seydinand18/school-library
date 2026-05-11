import { Component, inject } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { LoaderService } from '../../core/services/loader.service';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [ProgressSpinnerModule],
  template: `
    @if (loaderService.loading()) {
      <div style="position:fixed; inset:0; background:rgba(255,255,255,0.75); backdrop-filter:blur(2px); z-index:9999; display:flex; align-items:center; justify-content:center;">
        <div style="display:flex; flex-direction:column; align-items:center; gap:1rem;">
          <p-progressSpinner strokeWidth="3" fill="transparent"
            animationDuration=".8s" style="width:48px; height:48px;" />
          <span style="color:#475569; font-size:0.875rem; font-weight:500;">Chargement...</span>
        </div>
      </div>
    }
  `
})
export class LoaderComponent {
  loaderService = inject(LoaderService);
}
