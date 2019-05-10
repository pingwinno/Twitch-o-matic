package com.pingwinno.application;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDocumentModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetadataManager {
    private static final ReplaceOptions REPLACE_OPTIONS
            = ReplaceOptions.createReplaceOptions(new UpdateOptions().upsert(true));

    public static void importToDB(String user) {

        try (Stream<Path> walk = Files.walk(Paths.get(SettingsProperties.getRecordedStreamPath() + user))) {


            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.contains("metadata.json"))
                    .collect(Collectors.toList());
            MongoCollection collection = MongoDBHandler.getCollection(user);
            List<StreamDocumentModel> documentModels = new ArrayList<>();
            for (String jsonFile : result) {
                StreamDocumentModel streamDocumentModel = new ObjectMapper().readValue(new File(jsonFile), StreamDocumentModel.class);
                collection.replaceOne(new BasicDBObject("_id", streamDocumentModel.getUuid()), streamDocumentModel, REPLACE_OPTIONS);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void exportFromDB(String user) {
        MongoDBHandler.getCollection(user).find().forEach((Consumer<StreamDocumentModel>) x -> {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            try {
                writer.writeValue(new File(SettingsProperties.getRecordedStreamPath() + user + "/" + x.getUuid()
                        + "/metadata.json"), x);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
