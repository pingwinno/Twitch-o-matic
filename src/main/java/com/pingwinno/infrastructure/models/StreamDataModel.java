package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class StreamDataModel {
    @JsonProperty
    private UUID uuid;
    @JsonProperty
    private String date;
    @JsonProperty
    private String title;
    @JsonProperty
    private String game;
    @JsonProperty
    private String user;

    public StreamDataModel(UUID uuid, String date, String title, String game, String user) {

        this.uuid = uuid;
        this.title = title;
        this.date = date;
        this.game = game;
    }

    public StreamDataModel() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "StreamDataModel{" +
                "uuid=" + uuid +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", game='" + game + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


}
