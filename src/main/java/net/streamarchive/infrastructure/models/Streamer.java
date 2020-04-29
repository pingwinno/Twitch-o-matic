package net.streamarchive.infrastructure.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Streamer {
    @Id
    private String name;
    private String quality;
    private String redirect;

}
