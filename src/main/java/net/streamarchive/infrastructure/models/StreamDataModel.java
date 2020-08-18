package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.streamarchive.infrastructure.models.video.VodType;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class StreamDataModel {
    private int vodId;
    private String baseUrl;
    private boolean skipMuted;
    private int duration;
    private String previewUrl;
    private VodType vodType;
    private Map<String, QualityMetadata> qualities;
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
