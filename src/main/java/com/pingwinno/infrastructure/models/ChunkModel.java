package com.pingwinno.infrastructure.models;

import java.util.Objects;

public class ChunkModel {
    private String chunkName;
    private double time;

    public ChunkModel(String chunk, double time) {
        this.chunkName = chunk;
        this.time = time;
    }

    public String getChunkName() {
        return chunkName;
    }

    public void setChunkName(String chunkName) {
        this.chunkName = chunkName;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkModel that = (ChunkModel) o;
        return Double.compare(that.time, time) == 0 &&
                Objects.equals(chunkName, that.chunkName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkName, time);
    }
}
