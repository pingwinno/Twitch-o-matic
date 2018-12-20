package com.pingwinno.domain;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.pingwinno.infrastructure.SettingsProperties;

public class MongoDBHandler {
    private static DBCollection collection;

    public static void connect() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + SettingsProperties.getMongoDBAddress() + ":27017"));
        DB database = mongoClient.getDB("streams");
        collection = database.getCollection(SettingsProperties.getUser());
    }

    public static DBCollection getCollection() {
        return collection;
    }

}
