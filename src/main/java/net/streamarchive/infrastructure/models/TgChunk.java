package net.streamarchive.infrastructure.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class TgChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private UUID uuid;
    private String streamer;
    private String chunkName;
    private long messageID;
    private long size;
}
