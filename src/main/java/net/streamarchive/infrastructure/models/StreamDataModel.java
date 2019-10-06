package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class StreamDataModel {


    private int vodId;
    private String baseUrl;
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
    private String streamerName;

    public StreamDataModel() {

    }

    public String getStreamerName() {
        return streamerName;
    }

    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
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
                ", previewUrl='" + baseUrl + '\'' +
                ", skipMuted=" + skipMuted +
                ", uuid=" + uuid +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", game='" + game + '\'' +
                ", user='" + streamerName + '\'' +
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


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


}
