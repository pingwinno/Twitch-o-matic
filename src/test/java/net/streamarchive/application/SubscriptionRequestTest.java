package net.streamarchive.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubscriptionRequestTest {
    @Autowired
    SubscriptionRequest subscriptionRequest;

    @Test
    void sendSubscriptionRequest() throws IOException {

        assertEquals(subscriptionRequest.sendSubscriptionRequest("olyashaa"), 202);
    }
}