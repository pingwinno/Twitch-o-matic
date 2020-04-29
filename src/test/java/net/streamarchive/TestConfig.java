package net.streamarchive;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@Profile("test")
public class TestConfig {
    @Bean
    @Primary
    RestTemplate restTemplateWithCredentials(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
