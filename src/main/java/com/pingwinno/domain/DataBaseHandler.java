package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.slf4j.LoggerFactory;

public class DataBaseHandler {

    static private org.slf4j.Logger log = LoggerFactory.getLogger(DataBaseHandler.class);

    public static void writeToRemoteDB(StreamDocumentModel streamDocumentModel) {
        log.debug("Write to remote db...");
        JacksonDBCollection<StreamDocumentModel, String> coll = JacksonDBCollection.wrap(MongoDBHandler.getCollection(),
                StreamDocumentModel.class,
                String.class);
        WriteResult<StreamDocumentModel, String> result = coll.insert(streamDocumentModel);
        log.trace("Remote db endpoint: {}", SettingsProperties.getMongoDBAddress());

    }


}
