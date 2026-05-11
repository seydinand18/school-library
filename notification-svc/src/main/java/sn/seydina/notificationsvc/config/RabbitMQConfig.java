package sn.seydina.notificationsvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_EMPRUNT_CREE = "emprunt.cree";

    @Bean
    public Queue empruntCreeQueue() {
        return new Queue(QUEUE_EMPRUNT_CREE, true);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
