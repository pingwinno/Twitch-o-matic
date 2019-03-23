package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pingwinno.infrastructure.DocumentModelSerializer;

import java.util.Map;

@JsonSerialize(using = DocumentModelSerializer.class)
public class StreamDocumentModel {

    @JsonProperty("_id")
    private String uuid;
    private long date;
    private String title;
    private String game;
    private long duration;
    private Map<Integer, String> animatedPreviews;
    private Map<Integer, String> timelinePreviews;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @JsonProperty("animated_preview")
    public Map<Integer, String> getAnimatedPreviews() {
        return animatedPreviews;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(Map<Integer, String> animatedPreviews) {
        this.animatedPreviews = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public Map<Integer, String> getTimelinePreviews() {
        return timelinePreviews;
    }

    @JsonProperty("timeline_preview")
    public void setTimelinePreviews(Map<Integer, String> timelinePreviews) {
        this.timelinePreviews = timelinePreviews;
    }


}
