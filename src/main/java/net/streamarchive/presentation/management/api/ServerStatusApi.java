package net.streamarchive.presentation.management.api;

import net.streamarchive.application.StorageHelper;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.handlers.db.JpaDBHandler;
import net.streamarchive.infrastructure.handlers.db.RestDBHandler;
import net.streamarchive.infrastructure.models.StorageState;
import net.streamarchive.infrastructure.models.Streamer;
import net.streamarchive.infrastructure.models.StreamerNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * API returns storage state.
 */

@RestController
@RequestMapping("/api/v1/server")
public class ServerStatusApi {
    private final
    SettingsProperties settingsProperties;
    private final
    StorageHelper storageHelper;
    @Autowired
    JpaDBHandler jpaDBHandler;
    @Autowired
    RestDBHandler restDBHandler;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public ServerStatusApi(SettingsProperties settingsProperties, StorageHelper storageHelper) {

        this.settingsProperties = settingsProperties;

        this.storageHelper = storageHelper;
    }

    /**
     * Method returns list of free storage space per streamer.
     *
     * @return list of free storage space per streamer.
     */
    @GetMapping("/storage")
    public List<StorageState> getFreeStorage() throws IOException {
        return storageHelper.getStorageState();
    }

    /**
     * Method does import to local db from remote
     */
    @GetMapping("/import")
    public void importToLocalDb() throws StreamerNotFoundException {
        for (Streamer streamer : settingsProperties.getStreamers()) {
            for (net.streamarchive.infrastructure.models.Stream stream : restDBHandler.getAllStreams(streamer.getName())) {
                stream.setStreamer(streamer.getName());
                log.trace("Saving " + stream.toString());
                jpaDBHandler.addStream(stream);
                log.trace(stream.toString() + " saved");
            }
        }
    }

    /**
     * Method does export from local db to remote
     */
    @GetMapping("/export")
    public void exportFromLocalDb() throws StreamerNotFoundException {
        for (Streamer streamer : settingsProperties.getStreamers()) {
            for (net.streamarchive.infrastructure.models.Stream stream : jpaDBHandler.getAllStreams(streamer.getName())) {
                stream.setStreamer(streamer.getName());
                log.trace("Saving " + stream.toString());
                restDBHandler.addStream(stream);
                log.trace(stream.toString() + " saved");
            }
        }

    }

}
