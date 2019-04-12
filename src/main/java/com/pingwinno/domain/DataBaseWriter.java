package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class DataBaseWriter {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseWriter.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user) {
        if (MongoDBHandler.getCollection(user, StreamDocumentModel.class).find(
                eq("_id", streamDocumentModel.getUuid())).first() == null) {
            log.debug("Write to remote db...");
            MongoDBHandler.getCollection(user, StreamDocumentModel.class).insertOne(streamDocumentModel);
            log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());
        } else {
            log.debug("Update record...");
            MongoDBHandler.getCollection(user, StreamDocumentModel.class).
                    replaceOne(eq("_id", streamDocumentModel.getUuid()),
                            streamDocumentModel);
        }
    }
}
