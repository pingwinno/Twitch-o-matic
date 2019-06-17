package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

//@JsonSerialize(using = DocumentModelSerializer.class)
public class StreamDocumentOldModel {

    @BsonId
    private String uuid;
    private Date date;
    private String title;
    private String game;
    private long duration;
    @BsonProperty("animated_preview")
    private LinkedList<AnimatedPreview> animatedPreviews;
    @BsonProperty("timeline_preview")
    private LinkedHashMap<String, Preview> timelinePreviews;

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
    public LinkedList<AnimatedPreview> getAnimatedPreviews() {
        return animatedPreviews;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(LinkedList<AnimatedPreview> animatedPreviews) {
        this.animatedPreviews = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public LinkedHashMap<String, Preview> getTimelinePreviews() {
        return timelinePreviews;
    }

    @JsonProperty("timeline_preview")
    public void setTimelinePreviews(LinkedHashMap<String, Preview> timelinePreviews) {
        this.timelinePreviews = timelinePreviews;
    }


}
