package com.pingwinno.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.mongojack.JacksonDBCollection;
import org.slf4j.LoggerFactory;

public class DataBaseHandler {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseHandler.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel, String user) {
        log.debug("Write to remote db...");
        JacksonDBCollection<StreamDocumentModel, String> coll = JacksonDBCollection.wrap(MongoDBHandler.getCollection(user),
                StreamDocumentModel.class,
                String.class);
        DBObject obj = null;
        try {
            obj = (DBObject) JSON.parse(new ObjectMapper().writeValueAsString(streamDocumentModel));
        } catch (JsonProcessingException e) {
            log.error("parse error {}", e);
        }

        MongoDBHandler.getCollection(user).insert(obj);
        log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());

    }

    public static boolean isExist(StreamDocumentModel streamDocumentModel, String user) {
        JacksonDBCollection<StreamDocumentModel, String> coll = JacksonDBCollection.wrap(MongoDBHandler.getCollection(user),
                StreamDocumentModel.class,
                String.class);
        return (coll.findOneById(streamDocumentModel.getUuid()) == null);
    }


}
