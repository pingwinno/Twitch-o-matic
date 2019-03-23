package com.pingwinno.infrastructure;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.junit.jupiter.api.Test;

import java.util.Map;

class StreamDocumentCodecTest {
    Block<StreamDocumentModel> printBlock = new Block<StreamDocumentModel>() {
        @Override
        public void apply(final StreamDocumentModel document) {
            for (Map.Entry<Integer, String> stringEntry : document.getTimelinePreviews().entrySet()) {

            }
        }
    };

    @Test
    void decode() {
        MongoDBHandler.connect();
        MongoDBHandler.getCollection("unknowndevice");
        FindIterable<StreamDocumentModel> streamDocumentModels =
                MongoDBHandler.getCollection("unknowndevice").find();
        streamDocumentModels.forEach(printBlock);
    }

    @Test
    void encode() {
    }
}