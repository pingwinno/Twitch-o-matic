package net.streamarchive.domain;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StreamDocumentModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DataBaseWriter {
    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseWriter.class);
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SettingsProperties settingsProperties;

    public void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user, boolean writeToRemote) throws IOException {

        File file = new File(settingsProperties.getRecordedStreamPath() + user + "/" + streamDocumentModel.get_id()
                + "/metadata.json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, streamDocumentModel);


        log.debug("Write to remote db...");
        if (writeToRemote) {
            mongoTemplate.save(streamDocumentModel, user);
        }
    }
}
