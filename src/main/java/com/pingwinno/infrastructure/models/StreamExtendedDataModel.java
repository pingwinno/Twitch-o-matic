package com.pingwinno.infrastructure.models;

import java.util.UUID;

public class StreamExtendedDataModel extends StreamDataModel {


    private String vodId;
    private String previewUrl;
    private boolean skipMuted;

    public StreamExtendedDataModel() {
        super();
    }

    public StreamExtendedDataModel(UUID uuid, String date, String title, String game, String vodId, String previewUrl, boolean skipMuted) {
        super(uuid, date, title, game);
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    @Override
    public String toString() {
        return ("uuid:" + super.getUuid() + ", date:" + super.getDate() + "," +
                " title:" + super.getTitle() + ", game:" + super.getGame() +
                " , vod id:" + vodId + " , preview URL:" + previewUrl + " , skipMuted" + skipMuted);
    }
}
