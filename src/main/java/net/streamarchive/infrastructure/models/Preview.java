package net.streamarchive.infrastructure.models;

public class Preview {
    private String src;

    public Preview() {
    }

    public Preview(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
