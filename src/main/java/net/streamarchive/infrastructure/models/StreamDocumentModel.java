package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.LinkedHashMap;

//@JsonSerialize(using = DocumentModelSerializer.class)

public class StreamDocumentModel {

    @Id
    private String _id;
    private Date date;
    private String title;
    private String game;
    private long duration;

    private LinkedHashMap<String, String> animated_preview;

    private LinkedHashMap<String, Preview> timeline_preview;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
    public LinkedHashMap<String, String> getAnimatedPreviews() {
        return animated_preview;
    }

    @JsonProperty("animated_preview")
    public void setAnimatedPreviews(LinkedHashMap<String, String> animatedPreviews) {
        this.animated_preview = animatedPreviews;
    }

    @JsonProperty("timeline_preview")
    public LinkedHashMap<String, Preview> getTimeline_preview() {
        return timeline_preview;
    }

    @JsonProperty("timeline_preview")
    public void setTimeline_preview(LinkedHashMap<String, Preview> timeline_preview) {
        this.timeline_preview = timeline_preview;
    }


}
