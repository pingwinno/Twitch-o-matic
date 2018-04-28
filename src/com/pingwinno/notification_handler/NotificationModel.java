package com.pingwinno.notification_handler;

public class NotificationModel {
    private int id;
    private int user_id;
    private int game_id;
    private int [] community;
    private String type;
    private String title;
    private int viewer_count;
    private String started_at;
    private String language;
    private String thumbnail_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public void setCommunity(int[] community) {
        this.community = community;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewer_count(int viewer_count) {
        this.viewer_count = viewer_count;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }





    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public int[] getCommunity() {
        return community;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getViewer_count() {
        return viewer_count;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getLanguage() {
        return language;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

}
