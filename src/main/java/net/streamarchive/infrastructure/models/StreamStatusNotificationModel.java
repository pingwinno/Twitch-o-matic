package net.streamarchive.infrastructure.models;


public class StreamStatusNotificationModel {
    private NotificationDataModel[] data;

    public NotificationDataModel[] getData() {
        return data;
    }

    public void setData(NotificationDataModel[] data) {
        this.data = data;
    }
}
