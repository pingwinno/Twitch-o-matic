package net.streamarchive.infrastructure.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class TgChunk {
    @Id
    private long messageID;
    private UUID uuid;
    private String streamer;
    private String chunkName;
    private long size;
}
