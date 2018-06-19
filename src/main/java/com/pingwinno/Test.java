package com.pingwinno;

import com.pingwinno.application.twitch.playlist.handler.MasterPlaylistDownoader;
import com.pingwinno.application.twitch.playlist.handler.MasterPlaylistParser;
import com.pingwinno.application.twitch.playlist.handler.VodIdGetter;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws IOException, URISyntaxException {

        MasterPlaylistDownoader masterPlaylistDownoader = new MasterPlaylistDownoader();
        System.out.println(MasterPlaylistParser.parse(masterPlaylistDownoader.getPlaylistToken(VodIdGetter.getVodId("olyashaa"))));
        masterPlaylistDownoader.close();

        }


    }

