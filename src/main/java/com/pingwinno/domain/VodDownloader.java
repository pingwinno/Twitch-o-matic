package com.pingwinno.domain;

import com.pingwinno.application.twitch.playlist.handler.*;
import com.pingwinno.infrastructure.ChunkAppender;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.function.LongFunction;
import java.util.logging.Logger;

public class VodDownloader {
    private static Logger log = Logger.getLogger(VodDownloader.class.getName());
    private MasterPlaylistDownloader masterPlaylistDownloader = new MasterPlaylistDownloader();
    private MediaPlaylistDownloader mediaPlaylistDownloader = new MediaPlaylistDownloader();
    private URL website;
    private ReadableByteChannel rbc;
    private FileOutputStream fos;
    private LinkedHashSet<String> chunks = new LinkedHashSet<>();
    private LinkedList<String> downloadedChunks = new LinkedList<>();


    public void initializeDownload() {
        try {
           String m3u8Link = MasterPlaylistParser.parse(
                    masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
            String streamPath = StreamPathExtractor.extract(m3u8Link);
            System.out.println(streamPath);
            chunks = MediaPlaylistParser.parse(mediaPlaylistDownloader.getMediaPlaylist(m3u8Link));
            for (String chunkName : chunks) {
                website = new URL(streamPath + "/" + chunkName);
                rbc = Channels.newChannel(website.openStream());
                fos = new FileOutputStream(SettingsProperties.getRecordedStreamPath() + chunkName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                downloadedChunks.add(SettingsProperties.getRecordedStreamPath() + chunkName);
                log.info(chunkName + " complete");

            }
            this.compileChunks();
            System.out.println(chunks);
            this.recordCycle();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void refreshDownload() throws IOException {
        try {
                String m3u8Link = MasterPlaylistParser.parse(
                        masterPlaylistDownloader.getPlaylist(VodIdGetter.getVodId()));
                String streamPath = StreamPathExtractor.extract(m3u8Link);
                System.out.println(streamPath);
                BufferedReader reader = mediaPlaylistDownloader.getMediaPlaylist(m3u8Link);
                LinkedHashSet<String> refreshedPlaylist = MediaPlaylistParser.parse(reader);

                for (String chunkName : refreshedPlaylist) {
                    if (chunks.add(chunkName)) {
                        website = new URL(streamPath + "/" + chunkName);
                        rbc = Channels.newChannel(website.openStream());
                        fos = new FileOutputStream(SettingsProperties.getRecordedStreamPath() + chunkName);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        downloadedChunks.add(SettingsProperties.getRecordedStreamPath() + chunkName);
                        log.info(chunkName + " complete");
                    }
                }
                System.out.println(chunks);
                Thread.sleep(20 * 1000);
                this.compileChunks();
            } catch (IOException | InterruptedException | URISyntaxException e) {
                e.printStackTrace();
            }
            }


    public void compileChunks() {
        String firstChunk = SettingsProperties.getRecordedStreamPath() + "0.ts";
        while (downloadedChunks.iterator().hasNext()) {
            if (!downloadedChunks.peek().equals(firstChunk)) {
                ChunkAppender.copyfile(firstChunk, downloadedChunks.peek());
                new File(downloadedChunks.poll()).delete();
            } else {
                downloadedChunks.poll();
            }
        }
    }

    public void stopRecord() {
        try {

            fos.close();
            rbc.close();
            masterPlaylistDownloader.close();
            mediaPlaylistDownloader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
public void recordCycle() throws IOException {
        int counter = 0;
        while (VodIdGetter.getRecordStatus()){
            this.refreshDownload();
            log.info("Cycle: " + counter);
            counter++;
        }
        stopRecord();
}
}
