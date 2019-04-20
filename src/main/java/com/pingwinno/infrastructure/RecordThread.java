package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StreamDataModel;

public interface RecordThread {
    void start(StreamDataModel streamDataModel);

    void stop();
}
