
package net.streamarchive.infrastructure.models.video;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "animated_preview_url",
        "broadcast_id",
        "broadcast_type",
        "channel",
        "created_at",
        "delete_at",
        "description",
        "description_html",
        "fps",
        "game",
        "increment_view_count_url",
        "language",
        "length",
        "muted_segments",
        "preview",
        "published_at",
        "recorded_at",
        "resolutions",
        "restriction",
        "seek_previews_url",
        "status",
        "tag_list",
        "thumbnails",
        "title",
        "url",
        "viewable",
        "viewable_at",
        "views"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchVideoModel {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("animated_preview_url")
    private String animatedPreviewUrl;
    @JsonProperty("broadcast_id")
    private Integer broadcastId;
    @JsonProperty("broadcast_type")
    private String broadcastType;
    @JsonProperty("channel")
    private Channel channel;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("delete_at")
    private String deleteAt;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("description_html")
    private Object descriptionHtml;
    @JsonProperty("fps")
    private Map<String, Double> fps;
    @JsonProperty("game")
    private String game;
    @JsonProperty("increment_view_count_url")
    private String incrementViewCountUrl;
    @JsonProperty("language")
    private String language;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("muted_segments")
    private List<MutedSegment> mutedSegments = null;
    @JsonProperty("preview")
    private Preview preview;
    @JsonProperty("published_at")
    private String publishedAt;
    @JsonProperty("recorded_at")
    private String recordedAt;
    @JsonProperty("resolutions")
    private Map<String, String> resolutions;
    @JsonProperty("restriction")
    private String restriction;
    @JsonProperty("seek_previews_url")
    private String seekPreviewsUrl;
    @JsonProperty("status")
    private String status;
    @JsonProperty("tag_list")
    private String tagList;
    @JsonProperty("thumbnails")
    private Thumbnails thumbnails;
    @JsonProperty("title")
    private String title;
    @JsonProperty("url")
    private String url;
    @JsonProperty("viewable")
    private String viewable;
    @JsonProperty("viewable_at")
    private Object viewableAt;
    @JsonProperty("views")
    private Integer views;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("animated_preview_url")
    public String getAnimatedPreviewUrl() {
        return animatedPreviewUrl;
    }

    @JsonProperty("animated_preview_url")
    public void setAnimatedPreviewUrl(String animatedPreviewUrl) {
        this.animatedPreviewUrl = animatedPreviewUrl;
    }

    @JsonProperty("broadcast_id")
    public Integer getBroadcastId() {
        return broadcastId;
    }

    @JsonProperty("broadcast_id")
    public void setBroadcastId(Integer broadcastId) {
        this.broadcastId = broadcastId;
    }

    @JsonProperty("broadcast_type")
    public String getBroadcastType() {
        return broadcastType;
    }

    @JsonProperty("broadcast_type")
    public void setBroadcastType(String broadcastType) {
        this.broadcastType = broadcastType;
    }

    @JsonProperty("channel")
    public Channel getChannel() {
        return channel;
    }

    @JsonProperty("channel")
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("delete_at")
    public String getDeleteAt() {
        return deleteAt;
    }

    @JsonProperty("delete_at")
    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }

    @JsonProperty("description")
    public Object getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(Object description) {
        this.description = description;
    }

    @JsonProperty("description_html")
    public Object getDescriptionHtml() {
        return descriptionHtml;
    }

    @JsonProperty("description_html")
    public void setDescriptionHtml(Object descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    @JsonProperty("fps")
    public Map<String, Double> getFps() {
        return fps;
    }

    @JsonProperty("fps")
    public void setFps(Map<String, Double> fps) {
        this.fps = fps;
    }

    @JsonProperty("game")
    public String getGame() {
        return game;
    }

    @JsonProperty("game")
    public void setGame(String game) {
        this.game = game;
    }

    @JsonProperty("increment_view_count_url")
    public String getIncrementViewCountUrl() {
        return incrementViewCountUrl;
    }

    @JsonProperty("increment_view_count_url")
    public void setIncrementViewCountUrl(String incrementViewCountUrl) {
        this.incrementViewCountUrl = incrementViewCountUrl;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("muted_segments")
    public List<MutedSegment> getMutedSegments() {
        return mutedSegments;
    }

    @JsonProperty("muted_segments")
    public void setMutedSegments(List<MutedSegment> mutedSegments) {
        this.mutedSegments = mutedSegments;
    }

    @JsonProperty("preview")
    public Preview getPreview() {
        return preview;
    }

    @JsonProperty("preview")
    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    @JsonProperty("published_at")
    public String getPublishedAt() {
        return publishedAt;
    }

    @JsonProperty("published_at")
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @JsonProperty("recorded_at")
    public String getRecordedAt() {
        return recordedAt;
    }

    @JsonProperty("recorded_at")
    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    @JsonProperty("restriction")
    public String getRestriction() {
        return restriction;
    }

    @JsonProperty("restriction")
    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    @JsonProperty("seek_previews_url")
    public String getSeekPreviewsUrl() {
        return seekPreviewsUrl;
    }

    @JsonProperty("seek_previews_url")
    public void setSeekPreviewsUrl(String seekPreviewsUrl) {
        this.seekPreviewsUrl = seekPreviewsUrl;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("tag_list")
    public String getTagList() {
        return tagList;
    }

    @JsonProperty("tag_list")
    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    @JsonProperty("thumbnails")
    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    @JsonProperty("thumbnails")
    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("viewable")
    public String getViewable() {
        return viewable;
    }

    @JsonProperty("viewable")
    public void setViewable(String viewable) {
        this.viewable = viewable;
    }

    @JsonProperty("viewable_at")
    public Object getViewableAt() {
        return viewableAt;
    }

    @JsonProperty("viewable_at")
    public void setViewableAt(Object viewableAt) {
        this.viewableAt = viewableAt;
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

    @JsonProperty("resolutions")
    public Map<String, String> getResolutions() {
        return resolutions;
    }

    @JsonProperty("resolutions")
    public void setResolutions(Map<String, String> resolutions) {
        this.resolutions = resolutions;
    }

}
