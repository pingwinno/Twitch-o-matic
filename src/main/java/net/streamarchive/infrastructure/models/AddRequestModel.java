package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
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
}
