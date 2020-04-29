package net.streamarchive.infrastructure.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class Settings {
    @NonNull
    private String user;
    @NonNull
    private String userPass;
    @NonNull
    private String callbackAddress;
    @NonNull
    private String recordStreamPath;
    @NonNull
    private int serverPort;
    @NonNull
    private String remoteDBAddress;
    @NonNull
    private String dbUsername;
    @NonNull
    private String dbPassword;
    @NonNull
    private String clientID;
}
