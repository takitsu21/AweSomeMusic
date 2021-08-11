package util.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {
    private static MongoClient CLIENT;


//    static {
//
//    }

    public static MongoClient getClient() {
        if (CLIENT == null) {
            final MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                    .applicationName("AweSomeBot");
            final String username = "dylann";
            final String pwd = "3pNti27xpqkfhN8";
            final String host = "cluster0.qc0gw.mongodb.net:";
            final String port = "27017";
            settingsBuilder.applyConnectionString(new ConnectionString(
                    String.format("mongodb://%s:%s@%s%s/%s", username, pwd, host, port, "awesomebot")));
            CLIENT = MongoClients.create(settingsBuilder.build());

//            ClusterSettings clusterSettings = ClusterSettings.builder().hosts(Arrays.asList(
//                    new ServerAddress("cluster0.qc0gw.mongodb.net:27017"),
//                    new ServerAddress("cluster0-shard-00-01.qc0gw.mongodb.net:27017"),
//                    new ServerAddress("cluster0-shard-00-00.qc0gw.mongodb.net:27017"),
//                    new ServerAddress("cluster0-shard-00-02.qc0gw.mongodb.net:27017")))
//                    .applyConnectionString(new ConnectionString("mongodb://dylann:3pNti27xpqkfhN8@cluster0.qc0gw.mongodb.net/test?authSource=admin&replicaSet=atlas-14o7yb-shard-0&readPreference=primary&maxPoolSize=200"))
//                    .build();
//            MongoClientSettings settings = MongoClientSettings.builder().clusterSettings(clusterSettings).build();
//            client = MongoClients.create(settings);
//            ConnectionString connectionString = new ConnectionString("mongodb://dylann:3pNti27xpqkfhN8@cluster0-shard-00-00.qc0gw.mongodb.net:27017,cluster0-shard-00-01.qc0gw.mongodb.net:27017,cluster0-shard-00-02.qc0gw.mongodb.net:27017/awesomebot?replicaSet=atlas-14o7yb-shard-0&authSource=admin&w=majority");
//            MongoClientSettings settings = MongoClientSettings.builder().
//                    .applyConnectionString(connectionString)
//                    .build();
//            CLIENT = MongoClients.create(connectionString);
//            client = MongoClients.create("mongodb://dylann:3pNti27xpqkfhN8@cluster0.qc0gw.mongodb.net/test?authSource=admin&replicaSet=atlas-14o7yb-shard-0&readPreference=primary&maxPoolSize=200");
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
