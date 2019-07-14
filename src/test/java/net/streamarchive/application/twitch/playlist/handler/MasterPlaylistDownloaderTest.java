package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.StreamNotFoundExeption;
import org.apache.http.auth.AuthenticationException;
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
    void getPlaylist() throws URISyntaxException, StreamNotFoundExeption, InterruptedException, IOException, AuthenticationException {
        masterPlaylistDownloader.getPlaylist(446377443);

        assertNotEquals(masterPlaylistDownloader, masterPlaylistDownloader1);
    }
}