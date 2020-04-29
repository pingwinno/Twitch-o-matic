package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Data
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

    @JsonProperty("_id")
    public UUID getUuid() {
        return uuid;
    }

    @JsonProperty("_id")
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
