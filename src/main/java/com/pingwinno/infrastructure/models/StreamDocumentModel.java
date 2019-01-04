package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pingwinno.infrastructure.DocumentModelSerializer;

import java.util.Date;
import java.util.LinkedList;

@JsonSerialize(using = DocumentModelSerializer.class)
public class StreamDocumentModel {

    @JsonProperty("_id")
    private String uuid;
    private Date date;
    private String title;
    private String game;
    private long duration;
    private LinkedList<AnimatedPreviewModel> animatedPreviews;
    private LinkedList<TimelinePreviewModel> timelinePreviews;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public LinkedList<AnimatedPreviewModel> getAnimatedPreviews() {
        return animatedPreviews;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(LinkedList<AnimatedPreviewModel> animatedPreviews) {
        this.animatedPreviews = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public LinkedList<TimelinePreviewModel> getTimelinePreviews() {
        return timelinePreviews;
    }

    @JsonProperty("timeline_preview")
    public void setTimelinePreviews(LinkedList<TimelinePreviewModel> timelinePreviews) {
        this.timelinePreviews = timelinePreviews;
    }


}
