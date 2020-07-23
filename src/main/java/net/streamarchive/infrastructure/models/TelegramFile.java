package net.streamarchive.infrastructure.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class TelegramFile {
    @Id
    private long messageID;
    private String path;
    private long size;
}
