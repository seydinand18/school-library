import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ChatService {
  send(message: string, sessionId: string = 'default'): Observable<string> {
    return new Observable(observer => {
      fetch('/api/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'text/plain', 'X-Session-Id': sessionId },
        body: message
      }).then(async response => {
        const reader = response.body!.getReader();
        const decoder = new TextDecoder();
        while (true) {
          const { done, value } = await reader.read();
          if (done) { observer.complete(); break; }
          observer.next(decoder.decode(value));
        }
      }).catch(err => observer.error(err));
    });
  }
}
