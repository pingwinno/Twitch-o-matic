package net.streamarchive.infrastructure.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
public class Streamer {
    @Id
    private String name;
    private String[] qualities;
    private String redirect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQualities() {
        return Arrays.asList(qualities);
    }

    public void setQualities(List<String> qualities) {
        this.qualities = qualities.toArray(new String[0]);
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

}
