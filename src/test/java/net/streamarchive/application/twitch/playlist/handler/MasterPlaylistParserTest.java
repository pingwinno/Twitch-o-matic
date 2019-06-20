package net.streamarchive.application.twitch.playlist.handler;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


class MasterPlaylistParserTest {
    BufferedReader bufferedReader;

    @BeforeEach
    void init() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        FileReader fileReader =
                new FileReader(classLoader.getResource("master-playlist.m3u8").getFile());
        bufferedReader =
                new BufferedReader(fileReader);
    }

    @Test
    void parseChunked() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/chunked/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "chunked"), actual);

    }

    @Test
    void parse720p60() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/720p60/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "720p60"), actual);

    }

    @Test
    void parse720p30() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/720p30/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "720p30"), actual);

    }

    @Test
    void parse480p30() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/480p30/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "480p30"), actual);

    }

    @Test
    void parseAudioOnly() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/audio_only/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "audio_only"), actual);

    }

    @Test
    void parse360p30() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/360p30/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "360p30"), actual);

    }

    @Test
    void parse160p30() throws IOException {

        String actual = "https://vod-secure.twitch.tv/256827b0d6f9244d88fa_olyashaa_32110372480_1075523713/160p30/index-dvr.m3u8";
        assertEquals(MasterPlaylistParser.parse(bufferedReader, "160p30"), actual);

    }
}