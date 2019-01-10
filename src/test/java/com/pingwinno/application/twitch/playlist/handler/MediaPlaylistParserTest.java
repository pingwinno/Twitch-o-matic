package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.models.ChunkModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MediaPlaylistParserTest {
    BufferedReader bufferedReader;

    @BeforeEach
    void init() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        FileReader fileReader =
                new FileReader(classLoader.getResource("index-dvr.m3u8").getFile());
        bufferedReader =
                new BufferedReader(fileReader);
    }

    @Test
    void getChunksWithMuted() throws IOException {
        LinkedHashSet<ChunkModel> chunks = new LinkedHashSet<>();
        for (int i = 0; i < 58; i++) {

            if ((i == 14) || (i == 27)) {
                chunks.add(new ChunkModel(i + "-muted.ts", 10.000));
            } else {
                chunks.add(new ChunkModel(i + ".ts", 10.000));
            }
        }
        chunks.add(new ChunkModel("58.ts", 9.942));
        assertEquals(MediaPlaylistParser.getChunks(bufferedReader, false), chunks);
    }

    @Test
    void getChunksWithoutMuted() throws IOException {
        LinkedHashSet<ChunkModel> chunks = new LinkedHashSet<>();
        for (int i = 0; i < 58; i++) {
            if (!((i == 14) || (i == 27))) {
                chunks.add(new ChunkModel(i + ".ts", 10.000));
            }

        }
        chunks.add(new ChunkModel("58.ts", 9.942));

        assertEquals(MediaPlaylistParser.getChunks(bufferedReader, true), chunks);

    }

    @Test
    void getTotalSec() throws IOException {

        assertEquals(MediaPlaylistParser.getTotalSec(bufferedReader), 589);
    }
}