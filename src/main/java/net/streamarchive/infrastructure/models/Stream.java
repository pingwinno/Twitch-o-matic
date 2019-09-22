package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity

public class Stream implements Serializable {
    @Id
    private UUID uuid;
    private Date date;
    private String title;
    private String game;
    private long duration;
    private String streamer;

    public Stream() {
    }

    public Stream(UUID uuid, Date date, String title, String game, long duration, String streamer) {
        this.uuid = uuid;
        this.date = date;
        this.title = title;
        this.game = game;
        this.duration = duration;
        this.streamer = streamer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return uuid.equals(stream.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @JsonProperty("_id")
    public UUID getUuid() {
        return uuid;
    }

    @JsonProperty("_id")
    public void setUuid(UUID uuid) {
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

    public String getStreamer() {
        return streamer;
    }

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "uuid=" + uuid +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", game='" + game + '\'' +
                ", duration=" + duration +
                ", streamer='" + streamer + '\'' +
                '}';
    }
}
