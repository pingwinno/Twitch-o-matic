package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class DataBaseWriter {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseWriter.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user) {
        if (MongoDBHandler.getCollection(user, StreamDocumentModel.class).find(
                new Document("_id", streamDocumentModel.getUuid())) == null) {
            log.debug("Write to remote db...");
            MongoDBHandler.getCollection(user, StreamDocumentModel.class).insertOne(streamDocumentModel);
            log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());
        } else {
            MongoDBHandler.getCollection(user, StreamDocumentModel.class).
                    replaceOne(eq("_id", streamDocumentModel.getUuid()),
                            streamDocumentModel);
        }
    }
}
