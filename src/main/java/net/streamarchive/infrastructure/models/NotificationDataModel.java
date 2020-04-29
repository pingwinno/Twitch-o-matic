package net.streamarchive.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDataModel {
    private String id;
    private String[] community_ids;
    private String user_id;
    private String user_name;
    private String game_id;
    private String type;
    private String title;
    private String viewer_count;
    private String started_at;
    private String language;
    private String thumbnail_url;
}
