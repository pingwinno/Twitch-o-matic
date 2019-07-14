package net.streamarchive.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.domain.DataBaseWriter;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StorageState;
import net.streamarchive.infrastructure.models.StreamDocumentModel;
import net.streamarchive.infrastructure.models.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * API returns storage state.
 */
@PreAuthorize("#username == authentication.principal.username")
@RestController
@RequestMapping("/api/v1/server")
public class ServerStatusApi {
    private final
    DataBaseWriter dataBaseWriter;
    private final
    MongoTemplate mongoTemplate;
    private final
    SettingsProperties settingsProperties;
    private final
    StorageHelper storageHelper;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public ServerStatusApi(DataBaseWriter dataBaseWriter, MongoTemplate mongoTemplate, SettingsProperties settingsProperties, StorageHelper storageHelper) {
        this.dataBaseWriter = dataBaseWriter;
        this.mongoTemplate = mongoTemplate;
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
     * Method does import to local json files from MongoDB
     */
    @GetMapping("/import")
    public void importToLocalDb() {
        try {
            for (User user : settingsProperties.getUsers()) {
                for (StreamDocumentModel stream : mongoTemplate.findAll(StreamDocumentModel.class, user.getUser())) {
                    dataBaseWriter.writeToRemoteDB(stream, user.getUser(), false);
                }
            }
        } catch (IOException e) {
            throw new InternalServerErrorExeption();
        }
    }

    /**
     * Method does export from local db to MongoDB
     *
     */
    @GetMapping("/export")
    public void exportFromLocalDb() {
        ObjectMapper objectMapper = new ObjectMapper();

        for (User user : settingsProperties.getUsers()) {

            try (Stream<java.nio.file.Path> walk = Files.walk(Paths.get(settingsProperties.getRecordedStreamPath() + user))) {

                List<String> result = walk.filter(Files::isDirectory)
                        .map(Path::toString).collect(Collectors.toList());

                result.forEach(x -> {
                    try {
                        dataBaseWriter.writeToRemoteDB(objectMapper.readValue(x, StreamDocumentModel.class), user.getUser(), true);
                    } catch (IOException e) {
                        throw new InternalServerErrorExeption();
                    }
                });

            } catch (IOException e) {
                throw new InternalServerErrorExeption();
            }
        }
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private class InternalServerErrorExeption extends RuntimeException {
    }


}
