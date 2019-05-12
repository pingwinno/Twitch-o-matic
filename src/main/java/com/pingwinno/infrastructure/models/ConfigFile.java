package com.pingwinno.infrastructure.models;

import java.util.ArrayList;

public class ConfigFile {
    private String callbackAddress;
    private int twitchServerPort;
    private int managementServerPort;
    private int websocketServerPort;
    private ArrayList<String> users;
    private String recordedStreamPath;
    private boolean h2ConsoleIsEnabled;
    private String h2User;
    private String h2Password;
    private String streamQuality;
    private String mongoDBAddress;
    private String mongoDBName;

    public String getCallbackAddress() {
        return callbackAddress;
    }

    public void setCallbackAddress(String callbackAddress) {
        this.callbackAddress = callbackAddress;
    }

    public int getTwitchServerPort() {
        return twitchServerPort;
    }

    public void setTwitchServerPort(int twitchServerPort) {
        this.twitchServerPort = twitchServerPort;
    }

    public int getManagementServerPort() {
        return managementServerPort;
    }

    public void setManagementServerPort(int managementServerPort) {
        this.managementServerPort = managementServerPort;
    }

    public int getWebsocketServerPort() {
        return websocketServerPort;
    }

    public void setWebsocketServerPort(int websocketServerPort) {
        this.websocketServerPort = websocketServerPort;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getRecordedStreamPath() {
        return recordedStreamPath;
    }

    public void setRecordedStreamPath(String recordedStreamPath) {
        this.recordedStreamPath = recordedStreamPath;
    }

    public boolean isH2ConsoleIsEnabled() {
        return h2ConsoleIsEnabled;
    }

    public void setH2ConsoleIsEnabled(boolean h2ConsoleIsEnabled) {
        this.h2ConsoleIsEnabled = h2ConsoleIsEnabled;
    }

    public String getH2User() {
        return h2User;
    }

    public void setH2User(String h2User) {
        this.h2User = h2User;
    }

    public String getH2Password() {
        return h2Password;
    }

    public void setH2Password(String h2Password) {
        this.h2Password = h2Password;
    }

    public String getStreamQuality() {
        return streamQuality;
    }

    public void setStreamQuality(String streamQuality) {
        this.streamQuality = streamQuality;
    }

    public String getMongoDBAddress() {
        return mongoDBAddress;
    }

    public void setMongoDBAddress(String mongoDBAddress) {
        this.mongoDBAddress = mongoDBAddress;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }
}
