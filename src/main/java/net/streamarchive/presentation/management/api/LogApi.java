package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

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
    SettingsProvider settingsProperties;
    @Autowired
    StatusRepository statusRepository;

    /**
     * This method returns record log
     *
     * @param uuid is a record id
     * @return log file of stream
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("uuid") String uuid) {
        String filePath = settingsProperties.getRecordedStreamPath() + statusRepository.findByUuid(UUID.fromString(uuid)).get(0).getUser()
                + "/" + uuid + "/log.log";
        return new FileSystemResource(Paths.get(filePath));
    }


}
