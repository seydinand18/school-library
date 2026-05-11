package sn.seydina.notificationsvc;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class NotificationSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationSvcApplication.class, args);
    }

}
