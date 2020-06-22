package net.streamarchive;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.FileNotFoundException;
import java.io.PrintStream;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebSocket

public class TwitchOMaticApplication {
    public static void main(String[] args) throws FileNotFoundException {

        SpringApplication.run(TwitchOMaticApplication.class, args);

    }
}
