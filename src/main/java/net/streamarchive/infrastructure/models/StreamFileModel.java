package net.streamarchive.infrastructure.models;

import lombok.Data;

@Data
public class StreamFileModel {
    private final String baseUrl;
    private final String basePath;
    private final String fileName;
    private final double duration;
}
