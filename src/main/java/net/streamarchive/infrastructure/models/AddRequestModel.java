package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public class AddRequestModel {
    //Should contain "user"(live broadcasts) or "vod"(past broadcasts) value.
    private String type;

    //Should contain chanel name or vod ID
    private String value;

    //May contain parent streamer name
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String writeTo;

    //Use for skipp muted chunks
    private boolean skipMuted;
    //UUID of saved stream
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getWriteTo() {
        return writeTo;
    }

    public void setWriteTo(String writeTo) {
        this.writeTo = writeTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSkipMuted() {
        return skipMuted;
    }

    public void setSkipMuted(boolean skipMuted) {
        this.skipMuted = skipMuted;
    }
}
