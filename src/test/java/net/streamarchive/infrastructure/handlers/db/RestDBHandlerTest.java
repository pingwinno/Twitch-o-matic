package net.streamarchive.infrastructure.handlers.db;

import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

@TestPropertySource(locations = "classpath:test.properties")
class RestDBHandlerTest {
    static Random r = new Random();
    static Stream stream;
    @Autowired
    RestDBHandler restDBHandler;

    @BeforeAll
    public static void setup() {
        stream = new Stream(UUID.randomUUID(), Date.from(Instant.now()),
                "стримфест угу", "Just Chatting", r.nextInt(), "albisha");
    }

    @Test
    void getAllStreams() throws StreamerNotFoundException {
        restDBHandler.getAllStreams("albisha").forEach(System.out::println);
    }

    @Test
    void getStream() throws StreamNotFoundException {
        Stream stream = restDBHandler.getStream("albisha", UUID.fromString("eacb9d5d-2db2-4de4-a7df-be173b6ff22d"));
        assertEquals("eacb9d5d-2db2-4de4-a7df-be173b6ff22d", stream.getUuid().toString());
    }

    @Test
    void addStream() throws IOException, StreamNotFoundException {
        restDBHandler.addStream(stream);
        assertEquals(restDBHandler.getStream(stream.getStreamer(), stream.getUuid()), stream);
    }

    @Test
    void updateStream() throws IOException, StreamNotFoundException {
        stream.setDuration(r.nextInt());
        restDBHandler.addStream(stream);
        assertEquals(restDBHandler.getStream(stream.getStreamer(), stream.getUuid()), stream);
    }

    @Test
    void deleteStream() throws StreamNotFoundException {
        restDBHandler.deleteStream(stream);
        assertThrows(StreamNotFoundException.class, () -> {
            restDBHandler.getStream(stream.getStreamer(), stream.getUuid());
        });
    }
}