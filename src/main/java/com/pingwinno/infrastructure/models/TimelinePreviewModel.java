package com.pingwinno.infrastructure.models;

//@JsonSerialize(using = PreviewModelSerializer.class)
public class TimelinePreviewModel {

    private int index;

    private LinkModel src;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;

    }

    public LinkModel getLink() {
        return src;
    }

    public void setLink(LinkModel src) {
        this.src = src;
    }


}
