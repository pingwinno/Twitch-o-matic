package com.pingwinno.infrastructure.models;

public class ValidationDataModel {

    private StreamDataModel streamDataModel;
    private boolean skipMuted;
    private String vodId;

    public ValidationDataModel(StreamDataModel streamDataModel, boolean skipMuted, String vodId) {
        this.streamDataModel = streamDataModel;
        this.skipMuted = skipMuted;
        this.vodId = vodId;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public StreamDataModel getStreamDataModel() {
        return streamDataModel;
    }

    public void setStreamDataModel(StreamDataModel streamDataModel) {
        this.streamDataModel = streamDataModel;
    }

    public boolean isSkipMuted() {
        return skipMuted;
    }

    public void setSkipMuted(boolean skipMuted) {
        this.skipMuted = skipMuted;
    }
}
