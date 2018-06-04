package com.pingwinno.infrastructure;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SettingsProperties {
    private final static String PROPSFILE = "config.prop";

    private static Properties props;

    private static Properties getProperties() throws IOException {
        try {

            if (props == null) {
                props = new Properties();
                props.load(new FileInputStream(new File(PROPSFILE)));
            }
        }catch (FileNotFoundException e){
            System.err.println("config.prop not found");
            System.err.println("Place config.prop in Twitch-o-matic folder and try again");
            System.exit(1);
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

    public static String getGooglePhotosPath() {
        String googlePhotosPath = null;
        try {
            googlePhotosPath = getProperties().getProperty("GooglePhotosPath");
        } catch (IOException e) {
            System.err.println("Can't read GooglePhotosPath");
            e.printStackTrace();
        }
        return googlePhotosPath;
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

    public static String getServiceAccountName(){
        String serviceAccountName = null;
        try {
            serviceAccountName = getProperties().getProperty("ServiceAccountName");
        } catch (IOException e) {
            System.err.println("Can't read ServiceAccountName");
            e.printStackTrace();
        }
        return serviceAccountName;
    }
    public static String getPrivateKeyFileName(){
        String privateKeyFileName = null;
        try {
            privateKeyFileName = getProperties().getProperty("PrivateKeyFileName");
        } catch (IOException e) {
            System.err.println("Can't read PrivateKeyFileName");
            e.printStackTrace();
        }
        return privateKeyFileName;
    }

}





