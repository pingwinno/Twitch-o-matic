package net.streamarchive.infrastructure.models;

import lombok.Data;

@Data
public class StorageState {
    private String user;
    private int freeStorage;
    private int totalStorage;
    private double usedStorage;

    public void setUsedStorage(double usedStorage) {
        this.usedStorage = Math.floor(usedStorage * 100) / 100;
        ;
    }
}
