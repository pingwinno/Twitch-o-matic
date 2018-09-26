package com.pingwinno.infrastructure.models;

import java.util.UUID;

public class StreamRecordRecoveryListModel {
    private UUID uuid;
    private String vodId;

    StreamRecordRecoveryListModel (UUID uuid, String vodId){
        this.uuid = uuid;
        this.vodId = vodId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }






}
