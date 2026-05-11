package sn.seydina.chatbotsvc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory,
                                 ToolCallbackProvider toolCallbackProvider) {
        return builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem("""
                        Tu es l'assistant IA de la bibliothèque scolaire. Tu es sympathique et accueillant.
                        Tu peux répondre aux salutations et questions générales de politesse normalement.
                        Pour les questions sur les livres, les emprunts et les membres, tu DOIS obligatoirement appeler un outil — ne jamais inventer de données.
                        Si un outil ne retourne rien, dis que tu n'as pas trouvé.
                        Pour les sujets sans rapport avec la bibliothèque (météo, politique, etc.), redirige poliment vers ton domaine.
                        Réponds toujours en français, en langage naturel et conversationnel.
                        N'utilise jamais de markdown (pas de **, pas de #, pas de tirets de liste). Écris des phrases normales.
                        """)
                .build();
    }
}
