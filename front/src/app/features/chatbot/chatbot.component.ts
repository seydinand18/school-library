import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ChatService } from '../../core/services/chat.service';
import { marked } from 'marked';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

interface Message { role: 'user' | 'bot'; content: string; html?: SafeHtml; }

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [FormsModule, ButtonModule, InputTextModule],
  host: { ngSkipHydration: 'true' },
  templateUrl: './chatbot.component.html',
  styles: [`
    .dot-pulse { width:6px; height:6px; background:#94a3b8; border-radius:50%; animation:pulse 1s infinite; }
    @keyframes pulse { 0%,100%{opacity:0.4;transform:scale(0.8)} 50%{opacity:1;transform:scale(1)} }
  `]
})
export class ChatbotComponent {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;
  private chatService = inject(ChatService);
  private sanitizer = inject(DomSanitizer);

  private toHtml(md: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(marked.parse(md) as string);
  }

  messages = signal<Message[]>([]);
  loading = signal(false);
  input = '';

  suggestions = [
    '📚 Quels livres sont disponibles ?',
    '⏰ Quels emprunts sont en retard ?',
    '👥 Combien de membres sont inscrits ?',
    '🔖 Quel membre a le plus d\'emprunts ?'
  ];

  envoyerSuggestion(text: string) { this.input = text; this.envoyer(); }

  envoyer() {
    const text = this.input.trim();
    if (!text || this.loading()) return;
    this.input = '';
    this.messages.update(m => [...m, { role: 'user', content: text }]);
    this.loading.set(true);
    const botIndex = this.messages().length;
    this.messages.update(m => [...m, { role: 'bot', content: '' }]);
    let botContent = '';
    this.chatService.send(text).subscribe({
      next: chunk => {
        botContent += chunk;
        this.messages.update(m => m.map((msg, i) => i === botIndex ? { ...msg, content: botContent, html: this.toHtml(botContent) } : msg));
        this.scrollBottom();
      },
      complete: () => { this.loading.set(false); this.scrollBottom(); },
      error: () => {
        this.loading.set(false);
        this.messages.update(m => m.map((msg, i) => i === botIndex ? { ...msg, content: 'Erreur de connexion.', html: this.toHtml('Erreur de connexion.') } : msg));
      }
    });
  }

  private scrollBottom() {
    setTimeout(() => {
      const el = this.messagesContainer?.nativeElement;
      if (el) el.scrollTop = el.scrollHeight;
    }, 50);
  }
}
