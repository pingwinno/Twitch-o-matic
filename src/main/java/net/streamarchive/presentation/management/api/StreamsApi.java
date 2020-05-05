package net.streamarchive.presentation.management.api;


import net.streamarchive.domain.service.StreamsService;
import net.streamarchive.infrastructure.models.AddRequestModel;
import net.streamarchive.infrastructure.models.Stream;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API for database and streams management
 * Endpoint {@code /database}
 */
@RestController
@RequestMapping("/api/v1/database/streams")
public class StreamsApi {

    @Autowired
    private StreamsService streamsService;

    /**
     * Start recording new stream
     * Endpoint {@code /database/add}
     *
     * @param requestModel request params
     * @see AddRequestModel for review json fields
     */

    @RequestMapping(method = RequestMethod.PUT)
    public void startRecord(@RequestBody AddRequestModel requestModel) {
        streamsService.startRecord(requestModel);
    }

    /**
     * Delete stream from database
     * Endpoint {@code /database/{user}/{uuid}/}
     *
     * @param uuid        UUID of stream
     * @param user        name of streamer
     * @param deleteMedia if "true" also delete stream from storage
     * @throws net.streamarchive.infrastructure.exceptions.NotModifiedException if can't delete stream from storage (right issue/etc)
     */
    @RequestMapping(value = "{user}/{uuid}", method = RequestMethod.DELETE)
    public void deleteStream(@PathVariable("uuid") UUID uuid, @PathVariable("user") String user, @RequestParam("deleteMedia") String deleteMedia) {
        streamsService.deleteStream(uuid, user, deleteMedia);
    }

    /**
     * Update database record
     * Endpoint {@code /database/{user}/{uuid}/}
     *
     * @param user      name of streamer
     * @param uuid      UUID of stream
     * @param dataModel updated metadata
     * @see StreamDataModel for see required fields
     */
    @RequestMapping(value = "/{user}/{uuid}", method = RequestMethod.PATCH)
    public void updateStream(@PathVariable("uuid") String uuid, @PathVariable("user") String user, @RequestBody StreamDataModel dataModel) {
        streamsService.updateStream(UUID.fromString(uuid), user, dataModel);
    }

    /**
     * Return streams array of selected streamer
     * Endpoint {@code /database/streams}
     *
     * @param user streamer name
     * @return array of streamer streams
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public List<Stream> getStreamsList(@PathVariable("user") String user) {
        return streamsService.getStreams(user);
    }

}
