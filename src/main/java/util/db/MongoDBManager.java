package util.db;

import bot.AweSomeBot;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {
    private static MongoClient CLIENT;

    public static MongoClient getClient() {
        if (CLIENT == null) {
            final MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                    .applicationName("AweSomeBot");
            final String username = AweSomeBot.getCfctx().getMONGO_USERNAME();
            final String pwd = AweSomeBot.getCfctx().getMONGO_PWD();
            final String host = AweSomeBot.getCfctx().getMONGO_HOST();
            final String port = AweSomeBot.getCfctx().getMONGO_PORT();
            settingsBuilder.applyConnectionString(new ConnectionString(
                    String.format("mongodb://%s:%s@%s%s/%s", username, pwd, host, port, "awesomebot")));
            CLIENT = MongoClients.create(settingsBuilder.build());
        }

        return CLIENT;
    }


    public static MongoDatabase getDatabase(String db) {
        return getClient().getDatabase(db);
    }

    public static MongoCollection<Document> getCollection(String db, String collectionName) {
        return getDatabase(db).getCollection(collectionName);
    }
}
