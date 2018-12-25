package com.pingwinno.infrastructure.models;

import java.util.UUID;

public class StreamExtendedDataModel extends StreamDataModel {


    private String vodId;
    private String previewUrl;
    private boolean skipMuted;


    public StreamExtendedDataModel() {
        super();
    }

    public StreamExtendedDataModel(UUID uuid, String date, String title, String game, String user, String vodId, String previewUrl, boolean skipMuted) {
        super(uuid, date, title, game, user);
        this.vodId = vodId;
        this.previewUrl = previewUrl;
        this.skipMuted = skipMuted;
    }


    public boolean isSkipMuted() {
        return skipMuted;
    }

    public void setSkipMuted(boolean skipMuted) {
        this.skipMuted = skipMuted;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    @Override
    public String toString() {
        return super.toString() + "StreamExtendedDataModel{" +
                "vodId='" + vodId + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", skipMuted=" + skipMuted +
                '}';
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }


}
