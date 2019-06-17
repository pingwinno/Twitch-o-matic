package net.streamarchive.domain;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.StreamDocumentModel;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.LoggerFactory;


public class MongoDBHandler {
    private static MongoDatabase database;
    private static MongoClient mongoClient;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MongoDBHandler.class);

    public static void connect() {
        CodecRegistry registry;
        registry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = MongoClients.create(SettingsProperties.getMongoDBAddress());
        String dbName = "streams";
        if (!SettingsProperties.getMongoDBName().trim().isEmpty()) {
            dbName = SettingsProperties.getMongoDBName().trim();
        }
        log.trace(dbName);
        database = mongoClient.getDatabase(dbName).withCodecRegistry(registry);
    }

    public static void disconnect() {
        mongoClient.close();
    }


    public static MongoCollection getCollection(String user) {
        if (database == null) connect();
        return database.getCollection(user, StreamDocumentModel.class);
    }

    public static MongoCollection getCollection(String user, Class aClass) {
        if (database == null) connect();
        return database.getCollection(user, aClass);
    }


}
