package com.pingwinno.infrastructure.models;

import java.util.UUID;

public class ValidationDataModel {

    private UUID uuid;
    private boolean skipMuted;
    private String vodId;

    public ValidationDataModel(UUID uuid, boolean skipMuted, String vodId) {
        this.uuid = uuid;
        this.skipMuted = skipMuted;
        this.vodId = vodId;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isSkipMuted() {
        return skipMuted;
    }

    public void setSkipMuted(boolean skipMuted) {
        this.skipMuted = skipMuted;
    }
}
