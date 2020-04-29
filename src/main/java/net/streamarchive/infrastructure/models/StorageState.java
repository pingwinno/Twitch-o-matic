package net.streamarchive.infrastructure.models;

import lombok.Data;

@Data
public class StorageState {
    private String user;
    private int freeStorage;
    private int totalStorage;
}
