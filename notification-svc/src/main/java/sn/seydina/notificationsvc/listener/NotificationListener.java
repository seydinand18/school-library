package sn.seydina.notificationsvc.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import sn.seydina.notificationsvc.config.RabbitMQConfig;
import sn.seydina.notificationsvc.event.EmpruntEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMPRUNT_CREE)
    public void onEmpruntCree(Message amqpMessage) {
        String rawMessage = new String(amqpMessage.getBody());
        log.info("=== MESSAGE REÇU DE RABBITMQ ===");
        log.info("Contenu brut : {}", rawMessage);
        EmpruntEvent event;
        try {
            event = objectMapper.readValue(rawMessage, EmpruntEvent.class);
        } catch (Exception e) {
            log.error("Impossible de désérialiser le message : {}", rawMessage, e);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("seydinand18@gmail.com");
        message.setSubject("Confirmation d'emprunt — Bibliothèque scolaire");
        message.setText("""
                Bonjour %s %s,

                Votre emprunt a bien été enregistré.

                Livre     : %s
                Date      : %s
                Retour    : %s

                Merci de rendre le livre avant la date de retour.

                La Bibliothèque
                """.formatted(
                event.membrePrenom(),
                event.membreNom(),
                event.livreTitre(),
                event.dateEmprunt(),
                event.dateRetourPrevue()
        ));

        try {
            mailSender.send(message);
            log.info("Email envoyé à {}", event.membreEmail());
        } catch (Exception e) {
            log.error("Erreur envoi email", e);
        }
    }
}
