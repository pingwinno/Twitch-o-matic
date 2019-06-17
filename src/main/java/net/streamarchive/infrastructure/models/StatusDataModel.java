package net.streamarchive.infrastructure.models;

import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity

public class StatusDataModel {

    @Id
    private Integer vodId;
    private UUID uuid;
    private StartedBy startedBy;
    private String date;
    private State state;
    private String user;

    public StatusDataModel(int vodId, StartedBy startedBy, String date, State state, UUID uuid, String user) {
        this.vodId = vodId;
        this.startedBy = startedBy;
        this.date = date;
        this.state = state;
        this.uuid = uuid;
        this.user = user;
    }

    public StatusDataModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getVodId() {
        return vodId;
    }

    public void setVodId(Integer vodId) {
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
