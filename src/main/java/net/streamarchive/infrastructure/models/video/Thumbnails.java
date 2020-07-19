
package net.streamarchive.infrastructure.models.video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "large",
    "medium",
    "small",
    "template"
})
public class Thumbnails {

    @JsonProperty("large")
    private List<Large> large = null;
    @JsonProperty("medium")
    private List<Medium> medium = null;
    @JsonProperty("small")
    private List<Small> small = null;
    @JsonProperty("template")
    private List<Template> template = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("large")
    public List<Large> getLarge() {
        return large;
    }

    @JsonProperty("large")
    public void setLarge(List<Large> large) {
        this.large = large;
    }

    @JsonProperty("medium")
    public List<Medium> getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(List<Medium> medium) {
        this.medium = medium;
    }

    @JsonProperty("small")
    public List<Small> getSmall() {
        return small;
    }

    @JsonProperty("small")
    public void setSmall(List<Small> small) {
        this.small = small;
    }

    @JsonProperty("template")
    public List<Template> getTemplate() {
        return template;
    }

    @JsonProperty("template")
    public void setTemplate(List<Template> template) {
        this.template = template;
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
