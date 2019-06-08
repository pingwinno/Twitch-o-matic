package com.pingwinno.domain;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class DataBaseWriter {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseWriter.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user) throws IOException {

        File file = new File(SettingsProperties.getRecordedStreamPath() + user + "/" + streamDocumentModel.getUuid()
                + "/metadata.json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, streamDocumentModel);

        if (!SettingsProperties.getMongoDBAddress().trim().equals("")) {
            if (MongoDBHandler.getCollection(user).find(
                    eq("_id", streamDocumentModel.getUuid())).first() == null) {
                log.debug("Write to remote db...");
                MongoDBHandler.getCollection(user).insertOne(streamDocumentModel);
                log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());
            } else {
                log.debug("Update record...");
                MongoDBHandler.getCollection(user).
                        replaceOne(eq("_id", streamDocumentModel.getUuid()),
                                streamDocumentModel);
            }
        }
    }
}
