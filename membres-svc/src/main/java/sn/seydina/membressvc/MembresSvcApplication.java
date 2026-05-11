package sn.seydina.membressvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MembresSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MembresSvcApplication.class, args);
    }

}
