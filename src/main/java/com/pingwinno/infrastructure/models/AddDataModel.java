package com.pingwinno.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AddDataModel {
    private String type;
    private String value;
    private String writeTo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean skipMuted;

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
