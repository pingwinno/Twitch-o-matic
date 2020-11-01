package net.streamarchive.domain.service;

import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LogDownloadService {
    @Autowired
    SettingsProvider settingsProperties;
    @Autowired
    StatusRepository statusRepository;

    public Resource getLogFile(UUID uuid) {
        String filePath = settingsProperties.getRecordedStreamPath() + "/" + statusRepository.findByUuid(uuid).get(0).getUser()
                + "/" + uuid + "/log.log";
        return new FileSystemResource(Paths.get(filePath));
    }
}
