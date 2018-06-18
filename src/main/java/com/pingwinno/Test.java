package com.pingwinno;

import com.pingwinno.application.PlaylistGetter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) {
        PlaylistGetter playlistGetter = new PlaylistGetter();
        try {
            playlistGetter.getPlaylistToken("");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}
