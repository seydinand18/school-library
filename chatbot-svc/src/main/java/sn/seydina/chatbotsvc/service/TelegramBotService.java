package sn.seydina.chatbotsvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sn.seydina.chatbotsvc.agent.ChatAIAgent;

@Slf4j
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private final ChatAIAgent chatAIAgent;

    public TelegramBotService(@Value("${telegram.token}") String token, ChatAIAgent chatAIAgent) {
        super(token);
        this.chatAIAgent = chatAIAgent;
    }

    @Override
    public String getBotUsername() {
        return "Samakaagou_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        log.info("Message reçu de {} : {}", chatId, text);

        try {
            String reply = chatAIAgent.chat(text, chatId)
                    .collectList()
                    .block()
                    .stream()
                    .reduce("", String::concat);

            log.info("Réponse générée : {}", reply);
            execute(SendMessage.builder().chatId(chatId).text(reply).build());
        } catch (Exception e) {
            log.error("Erreur traitement message Telegram", e);
        }
    }
}
