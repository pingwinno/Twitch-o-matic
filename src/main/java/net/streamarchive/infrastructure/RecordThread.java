package net.streamarchive.infrastructure;

import net.streamarchive.infrastructure.models.StreamDataModel;

public interface RecordThread {
    void start(StreamDataModel streamDataModel);
    void stop();
}
