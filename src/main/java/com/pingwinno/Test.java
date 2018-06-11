package com.pingwinno;

import com.pingwinno.application.PlaylistGetter;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        PlaylistGetter playlistGetter = new PlaylistGetter();
        try {
            playlistGetter.getPlaylistToken("");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
