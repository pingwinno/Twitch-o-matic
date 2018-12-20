package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class StreamDocumentModel {

    @JsonProperty("_id")
    private String uuid;
    private String date;
    private String title;
    private String game;
    private String duration;
    private LinkedList<PreviewModel> animatedPreviews;
    private LinkedList<PreviewModel> timelinePreviews;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("animated_preview")
    public LinkedList<PreviewModel> getAnimatedPreviews() {
        return animatedPreviews;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(LinkedList<PreviewModel> animatedPreviews) {
        this.animatedPreviews = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public LinkedList<PreviewModel> getTimelinePreviews() {
        return timelinePreviews;
    }

    @JsonProperty("timeline_preview")
    public void setTimelinePreviews(LinkedList<PreviewModel> timelinePreviews) {
        this.timelinePreviews = timelinePreviews;
    }


}
