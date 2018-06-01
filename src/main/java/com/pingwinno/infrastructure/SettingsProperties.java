package com.pingwinno.infrastructure;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsProperties {
    public final static String PROPSFILE = "config.prop";

    private static Properties props;

    private static Properties getProperties() throws IOException {
        if (props == null) {
            props = new Properties();
            props.load(new FileInputStream(new File(PROPSFILE)));
        }
        return props;
    }

    public static String getCallbackAddress() {
        String callbackAddress = null;
        try {
            callbackAddress = getProperties().getProperty("CallbackAddress");
        } catch (IOException e) {
            System.err.println("Can't read CallbackAddress");
            e.printStackTrace();
        }
        return callbackAddress;
    }

    public static int getServerPort() {
        int serverPort = 0;
        try {
            serverPort = Integer.parseInt(getProperties().getProperty("ServerPort"));
        } catch (IOException e) {
            System.err.println("Can't read ServerPort");
            e.printStackTrace();
        }
        return serverPort;
    }

    public static String getUser() {
        String user = null;
        try {
            user = getProperties().getProperty("User");
        } catch (IOException e) {
            System.err.println("Can't read User");
            e.printStackTrace();
        }
        return user;
    }
    public static String getGoogleAccount() {
        String googleAccount = null;
        try {
            googleAccount = getProperties().getProperty("GoogleAccount");
        } catch (IOException e) {
            System.err.println("Can't read GoogleAccount");
            e.printStackTrace();
        }
        return googleAccount;
    }

    public static String getGoogleAccountPassword() {
        String googleAccountPassword = null;
        try {
            googleAccountPassword = getProperties().getProperty("GoogleAccountPassword");
        } catch (IOException e) {
            System.err.println("Can't read GoogleAccountPassword");
            e.printStackTrace();
        }
        return googleAccountPassword;
    }
    public static String getGooglePhotosAlbumURL() {
        String googlePhotosAlbumURL = null;
        try {
            googlePhotosAlbumURL = getProperties().getProperty("GooglePhotosAlbumURL");
        } catch (IOException e) {
            System.err.println("Can't read GooglePhotosAlbumURL");
            e.printStackTrace();
        }
        return googlePhotosAlbumURL;
    }

    public static String getRecordedStreamPath (){
        String recordedStreamPath = null;
        try {
            recordedStreamPath = getProperties().getProperty("RecordedStreamPath");
        } catch (IOException e) {
            System.err.println("Can't read RecordedStreamPath");
            e.printStackTrace();
        }
        return recordedStreamPath;
    }
    public static String getRecordMachineName (){
        String recordMachineName = null;
        try {
            recordMachineName = getProperties().getProperty("RecordMachineName");
        } catch (IOException e) {
            System.err.println("Can't read RecordMachineName");
            e.printStackTrace();
        }
        return recordMachineName;
    }
}





