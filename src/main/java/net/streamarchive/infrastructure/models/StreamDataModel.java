package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class StreamDataModel {

    private int vodId;
    private String baseUrl;
    private boolean skipMuted;
    @JsonProperty
    private UUID uuid;
    @JsonProperty
    private Date date;
    @JsonProperty
    private String title;
    @JsonProperty
    private String game;
    @JsonProperty
    private String streamerName;
}
