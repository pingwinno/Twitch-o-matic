package net.streamarchive;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebSocket

@EnableWebSocketMessageBroker
public class TwitchOMaticApplication {


    public static void main(String[] args) {
        SpringApplication.run(TwitchOMaticApplication.class, args);
    }


}
