package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.bson.Document;
import org.slf4j.LoggerFactory;

public class DataBaseHandler {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseHandler.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user) {
        log.debug("Write to remote db...");
        MongoDBHandler.getCollection(user).insertOne(streamDocumentModel);
        log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());

    }

    public static boolean isExist(StreamDocumentModel streamDocumentModel, String user) {
        return (MongoDBHandler.getCollection(user).find(
                new Document("_id", streamDocumentModel.getUuid())) == null);
    }


}
