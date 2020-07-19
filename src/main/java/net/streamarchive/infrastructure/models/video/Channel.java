
package net.streamarchive.infrastructure.models.video;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "broadcaster_language",
    "broadcaster_software",
    "broadcaster_type",
    "created_at",
    "description",
    "display_name",
    "followers",
    "game",
    "language",
    "logo",
    "mature",
    "name",
    "partner",
    "privacy_options_enabled",
    "private_video",
    "profile_banner",
    "profile_banner_background_color",
    "status",
    "updated_at",
    "url",
    "video_banner",
    "views"
})
public class Channel {

    @JsonProperty("_id")
    private Integer id;
    @JsonProperty("broadcaster_language")
    private String broadcasterLanguage;
    @JsonProperty("broadcaster_software")
    private String broadcasterSoftware;
    @JsonProperty("broadcaster_type")
    private String broadcasterType;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("description")
    private String description;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("followers")
    private Integer followers;
    @JsonProperty("game")
    private String game;
    @JsonProperty("language")
    private String language;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("mature")
    private Boolean mature;
    @JsonProperty("name")
    private String name;
    @JsonProperty("partner")
    private Boolean partner;
    @JsonProperty("privacy_options_enabled")
    private Boolean privacyOptionsEnabled;
    @JsonProperty("private_video")
    private Boolean privateVideo;
    @JsonProperty("profile_banner")
    private String profileBanner;
    @JsonProperty("profile_banner_background_color")
    private String profileBannerBackgroundColor;
    @JsonProperty("status")
    private String status;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("url")
    private String url;
    @JsonProperty("video_banner")
    private String videoBanner;
    @JsonProperty("views")
    private Integer views;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("broadcaster_language")
    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    @JsonProperty("broadcaster_language")
    public void setBroadcasterLanguage(String broadcasterLanguage) {
        this.broadcasterLanguage = broadcasterLanguage;
    }

    @JsonProperty("broadcaster_software")
    public String getBroadcasterSoftware() {
        return broadcasterSoftware;
    }

    @JsonProperty("broadcaster_software")
    public void setBroadcasterSoftware(String broadcasterSoftware) {
        this.broadcasterSoftware = broadcasterSoftware;
    }

    @JsonProperty("broadcaster_type")
    public String getBroadcasterType() {
        return broadcasterType;
    }

    @JsonProperty("broadcaster_type")
    public void setBroadcasterType(String broadcasterType) {
        this.broadcasterType = broadcasterType;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("followers")
    public Integer getFollowers() {
        return followers;
    }

    @JsonProperty("followers")
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    @JsonProperty("game")
    public String getGame() {
        return game;
    }

    @JsonProperty("game")
    public void setGame(String game) {
        this.game = game;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("logo")
    public String getLogo() {
        return logo;
    }

    @JsonProperty("logo")
    public void setLogo(String logo) {
        this.logo = logo;
    }

    @JsonProperty("mature")
    public Boolean getMature() {
        return mature;
    }

    @JsonProperty("mature")
    public void setMature(Boolean mature) {
        this.mature = mature;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("partner")
    public Boolean getPartner() {
        return partner;
    }

    @JsonProperty("partner")
    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    @JsonProperty("privacy_options_enabled")
    public Boolean getPrivacyOptionsEnabled() {
        return privacyOptionsEnabled;
    }

    @JsonProperty("privacy_options_enabled")
    public void setPrivacyOptionsEnabled(Boolean privacyOptionsEnabled) {
        this.privacyOptionsEnabled = privacyOptionsEnabled;
    }

    @JsonProperty("private_video")
    public Boolean getPrivateVideo() {
        return privateVideo;
    }

    @JsonProperty("private_video")
    public void setPrivateVideo(Boolean privateVideo) {
        this.privateVideo = privateVideo;
    }

    @JsonProperty("profile_banner")
    public String getProfileBanner() {
        return profileBanner;
    }

    @JsonProperty("profile_banner")
    public void setProfileBanner(String profileBanner) {
        this.profileBanner = profileBanner;
    }

    @JsonProperty("profile_banner_background_color")
    public String getProfileBannerBackgroundColor() {
        return profileBannerBackgroundColor;
    }

    @JsonProperty("profile_banner_background_color")
    public void setProfileBannerBackgroundColor(String profileBannerBackgroundColor) {
        this.profileBannerBackgroundColor = profileBannerBackgroundColor;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("video_banner")
    public String getVideoBanner() {
        return videoBanner;
    }

    @JsonProperty("video_banner")
    public void setVideoBanner(String videoBanner) {
        this.videoBanner = videoBanner;
    }

    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Integer views) {
        this.views = views;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
