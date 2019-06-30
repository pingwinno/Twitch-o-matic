package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.StreamNotFoundExeption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class MasterPlaylistDownloaderTest {
    @Autowired
    MasterPlaylistDownloader masterPlaylistDownloader;
    @Autowired
    MasterPlaylistDownloader masterPlaylistDownloader1;

    @Test
    void getPlaylist() throws URISyntaxException, StreamNotFoundExeption, InterruptedException, IOException {
        masterPlaylistDownloader.getPlaylist(444505062);
        masterPlaylistDownloader.getPlaylist(445418382);
        assertNotEquals(masterPlaylistDownloader, masterPlaylistDownloader1);
    }
}