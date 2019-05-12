package com.pingwinno.application;

import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.infrastructure.models.AnimatedPreview;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import com.pingwinno.infrastructure.models.StreamDocumentOldModel;
import org.bson.Document;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

class MetadataManagerTest {


    void importToDB() {
        //MongoDBHandler.connect();
        MetadataManager.importToDB("dedlim");
    }


    void exportFromDB() {
        MetadataManager.exportFromDB("albisha");
    }


    void convert() {
        List<StreamDocumentOldModel> streamDocumentOldModels =
                (List<StreamDocumentOldModel>) MongoDBHandler.getCollection("olyashaa",
                        StreamDocumentOldModel.class).find().into(new LinkedList());
        List<StreamDocumentModel> streamDocumentModels = new LinkedList<>();
        for (StreamDocumentOldModel streamDocumentOldModel : streamDocumentOldModels) {
            StreamDocumentModel streamDocumentModel = new StreamDocumentModel();
            streamDocumentModel.setUuid(streamDocumentOldModel.getUuid());
            streamDocumentModel.setDate(streamDocumentOldModel.getDate());
            streamDocumentModel.setDuration(streamDocumentOldModel.getDuration());
            streamDocumentModel.setGame(streamDocumentOldModel.getGame());
            streamDocumentModel.setTitle(streamDocumentOldModel.getTitle());
            streamDocumentModel.setTimelinePreviews(streamDocumentOldModel.getTimelinePreviews());

            LinkedHashMap<String, String> animated = new LinkedHashMap<>();

            for (AnimatedPreview animatedPreview : streamDocumentOldModel.getAnimatedPreviews()) {
                animated.put(String.valueOf(animatedPreview.getIndex()), animatedPreview.getSrc());
            }

            streamDocumentModel.setAnimatedPreviews(animated);

            MongoDBHandler.getCollection("olyashaa").replaceOne(new Document("_id", streamDocumentModel.getUuid()), streamDocumentModel);
        }


    }
}