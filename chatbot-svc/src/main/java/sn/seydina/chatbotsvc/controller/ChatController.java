package sn.seydina.chatbotsvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import sn.seydina.chatbotsvc.agent.ChatAIAgent;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatAIAgent chatAIAgent;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chat(
            @RequestBody String query,
            @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId) {
        return chatAIAgent.chat(query, sessionId);
    }
}
