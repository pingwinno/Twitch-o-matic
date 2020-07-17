package net.streamarchive.presentation.management.api;

import net.streamarchive.domain.service.LogDownloadService;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * This API uses for records log.
 * Endpoint {@code /log}
 */
@RestController
@RequestMapping("/api/v1/log")
public class LogApi {

    @Autowired
    private LogDownloadService service;

    /**
     * This method returns record log
     *
     * @param uuid is a record id
     * @return log file of stream
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET, produces ="text/plain")
    @ResponseBody
    public Resource getFile(@PathVariable("uuid") UUID uuid) {
        return service.getLogFile(uuid);
    }
}
