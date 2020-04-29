package net.streamarchive.infrastructure.models;

import lombok.Data;

@Data
public class StreamStatusNotificationModel {
    private NotificationDataModel[] data;
}
