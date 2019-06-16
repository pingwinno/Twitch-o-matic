package com.pingwinno.infrastructure.models;

public class StorageState {
    private String user;
    private int freeStorage;
    private int totalStorage;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getFreeStorage() {
        return freeStorage;
    }

    public void setFreeStorage(int freeStorage) {
        this.freeStorage = freeStorage;
    }

    public int getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(int totalStorage) {
        this.totalStorage = totalStorage;
    }

}
