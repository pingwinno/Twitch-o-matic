package com.pingwinno.domain;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.pingwinno.infrastructure.SettingsProperties;

public class MongoDBHandler {
    private static DB database;

    public static void connect() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + SettingsProperties.getMongoDBAddress() + ":27017"));
        database = mongoClient.getDB("streams");

    }

    public static DBCollection getCollection(String user) {

        return database.getCollection(user);
    }

}
