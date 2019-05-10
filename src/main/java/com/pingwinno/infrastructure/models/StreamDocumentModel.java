package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.Map;

//@JsonSerialize(using = DocumentModelSerializer.class)
public class StreamDocumentModel {

    @BsonId
    private String uuid;
    private Date date;
    private String title;
    private String game;
    private long duration;
    @BsonProperty("animated_preview")
    private Map<String, String> animatedPreviews;
    @BsonProperty("timeline_preview")
    private Map<String, Preview> timelinePreviews;

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
    public Map<String, String> getAnimatedPreviews() {
        return animatedPreviews;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(Map<String, String> animatedPreviews) {
        this.animatedPreviews = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public Map<String, Preview> getTimelinePreviews() {
        return timelinePreviews;
    }

    @JsonProperty("timeline_preview")
    public void setTimelinePreviews(Map<String, Preview> timelinePreviews) {
        this.timelinePreviews = timelinePreviews;
    }


}
