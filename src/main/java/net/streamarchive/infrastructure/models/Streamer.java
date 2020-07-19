package net.streamarchive.infrastructure.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Streamer {
    @Id
    private String name;
    @Column(name="quality")
    private String qualities;
    private String redirect;

    public List<String> getQualities() {
        return List.of(qualities.split(" "));
    }

    public void setQualities(List<String> qualities) {
        this.qualities = String.join(" ", qualities);
    }
}
