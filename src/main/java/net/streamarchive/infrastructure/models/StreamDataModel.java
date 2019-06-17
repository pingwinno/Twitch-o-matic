package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class StreamDataModel {


    private int vodId;
    private String previewUrl;
    private boolean skipMuted;
    @JsonProperty
    private UUID uuid;
    @JsonProperty
    private Date date;
    @JsonProperty
    private String title;
    @JsonProperty
    private String game;
    @JsonProperty
    private String user;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    @Override
    public String toString() {
        return "StreamDataModel{" +
                "vodId='" + vodId + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", skipMuted=" + skipMuted +
                ", uuid=" + uuid +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", game='" + game + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    public boolean isSkipMuted() {
        return skipMuted;
    }

    public void setSkipMuted(boolean skipMuted) {
        this.skipMuted = skipMuted;
    }

    public int getVodId() {
        return vodId;
    }

    public void setVodId(int vodId) {
        this.vodId = vodId;
    }


    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }


}
