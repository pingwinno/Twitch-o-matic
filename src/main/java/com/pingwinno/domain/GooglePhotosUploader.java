package com.pingwinno.domain;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GooglePhotosUploader {



    public void uploadRecordedStream(String filename) {
        PicasawebService service = new PicasawebService("unknown_device-Twitch_O_Matic-0.4");
        try {
            service.setUserCredentials(SettingsProperties.getGoogleAccount(), SettingsProperties.getGoogleAccountPassword());
        } catch (AuthenticationException e) {
            System.err.println("Authentication on Google failed" + e);
            e.printStackTrace();
        }
        URL albumPostUrl = null;
        try {
            albumPostUrl = new URL(SettingsProperties.getGooglePhotosAlbumURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        PhotoEntry recordedStream = new PhotoEntry();
        recordedStream.setTitle(new PlainTextConstruct(filename));
        recordedStream.setClient(SettingsProperties.getRecordMachineName());

        MediaFileSource myMedia = new MediaFileSource(new File(SettingsProperties.getRecordedStreamPath()+ filename ), "video/h264");
        recordedStream.setMediaSource(myMedia);

        PhotoEntry returnedVideo = null;
        try {
            returnedVideo = service.insert(albumPostUrl, recordedStream);
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }

// Fetching the video status (will always be "pending" right after posting a video).
        assert returnedVideo != null;
        String videoStatus = returnedVideo.getVideoStatus();
    }


}
