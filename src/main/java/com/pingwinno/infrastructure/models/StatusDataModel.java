package com.pingwinno.infrastructure.models;

import com.pingwinno.infrastructure.StartedBy;
import com.pingwinno.infrastructure.State;

import java.util.UUID;

public class StatusDataModel {
    private String vodId;
    private StartedBy startedBy;
    private String date;
    private State state;
    private UUID uuid;

    public StatusDataModel(String vodId, StartedBy startedBy, String date, State state, UUID uuid) {
        this.vodId = vodId;
        this.startedBy = startedBy;
        this.date = date;
        this.state = state;
        this.uuid = uuid;
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

    public StartedBy getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(StartedBy startedBy) {
        this.startedBy = startedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
