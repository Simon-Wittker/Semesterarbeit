package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.Metadata;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoConfigurationException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static project.simtv_mam_app.Messages.failedDBConnectionError;

/**
 * Eine Strategy-Implementierung, die als Ziel eine MongoDB hat.
 * @author Simon Wittker
 */
public class StrategyDB implements Strategy
{
    private final String dataSetName;
    private MongoDatabase database;
    private MongoClient mongoClient;
    private MongoCollection<Document> collectionDataset1;

    /**
     * Der Konstruktor.
     * Beim Aufruf initiiert er gleich die Verbindung zur MongoDB.
     * @param dataSetName Der Name des Ordners auf der DB.
     */
    public StrategyDB(String dataSetName)
    {
        this.dataSetName = dataSetName;
        createAConnectionToDB();
    }

    /**
     * Methode zur Initiierung der Verbindung zur MongoDB.
     * Dabei erstellt er einen MongoClient, mit der Methode "createAMongoClient()",
     * eine Verbindung zur Database, mit der Methode "createADatabase()",
     * und eine Verbindung zur Mongo-Collection, mit der Methode "createAMongoCollection()".
     * Wirft eine Exception, wenn die Verbindung nicht hergestellt werden kann.
     *
     */
    private void createAConnectionToDB()
    {
        createAMongoClient();
        createAMongoCollection(dataSetName);
        createADatabase();
        MongoCollection<Document> collection = database.getCollection("0.0.0.0/0");

        try
        {
            collection.find().first();
        } catch (MongoTimeoutException e)
        {
            e.printStackTrace();
            failedDBConnectionError();
        }
    }

    /**
     * Methode zum Kreieren eines MongoClient.
     * Wird in "createAConnectionToDB()" aufgerufen.
     */
    private void createAMongoClient()
    {
        try
        {
            ConnectionString connectionString = new ConnectionString(
                    "mongodb+srv://user:sPersAveP4S$W0rd@simtvcluster.gxig5.mongodb.net/" +
                            "SimTV_MAM?retryWrites=true&w=majority");

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
        } catch (MongoConfigurationException e)
        {
            e.printStackTrace();
            failedDBConnectionError();
        }
    }

    /**
     * Methode zum Verbinden mit der Mongo-Database.
     * Wird in "createAConnectionToDB()" aufgerufen.
     */
    private void createADatabase()
    {
        String databaseName = "SimTVCluster";
        database = mongoClient.getDatabase(databaseName);
    }

    /**
     * Methode zum Verbinden mit der Mongo-Collection.
     * Wird in "createAConnectionToDB()" aufgerufen.
     * @param nameOfDataSet der Name des DataSets,
     */
    private void createAMongoCollection(String nameOfDataSet)
    {
        collectionDataset1 = mongoClient.getDatabase("SimTV_MAM").getCollection(nameOfDataSet);
    }

    /**
     * Metadaten in ein File speichern.
     * @param metadata Metadaten-Objekt zum Speichern.
     */
    @Override
    public void save(Metadata metadata)
    {
        List<Document> metadataSet = new ArrayList<>();
        metadataSet.add(new Document("metadataID", metadata.getMetadataID()).append("fileID", metadata.getFileID())
                .append("fileTitle", metadata.getFileTitle()).append("fileType", metadata.getFileType())
                .append("fileSize", metadata.getFileSize()).append("fileSizeUnit", metadata.getFileSizeUnit())
                .append("fileDate", metadata.getFileDate()).append("fileAuthor", metadata.getFileAuthor())
                .append("fileCast", metadata.getFileCast()).append("description", metadata.getDescription())
                .append("saveLocation", metadata.getSaveLocation()));
        collectionDataset1.insertMany(metadataSet);
    }

    /**
     * Metadaten-File in der DB suchen.
     * @param searchStringArray       Such-Attribut, Suchbegriff und der Ort, der durchsucht werden soll.
     * @return                        Eine Liste mit einem String, der die Daten der Treffer beinhaltet.
     */
    @Override
    public List<String> search(String[] searchStringArray)
    {
        List<Document> metadataDoku;
        metadataDoku = collectionDataset1.find(Filters.eq(searchStringArray[0], searchStringArray[1])).into(new ArrayList<>());
        metadataDoku.addAll(collectionDataset1.find(Filters.gt(searchStringArray[0], searchStringArray[1])).into(new ArrayList<>()));

        List<String> metadataSet = new ArrayList<>();
        metadataDoku.forEach(d -> metadataSet.add(d.toJson()));

        return metadataSet;
    }

    /**
     * Bestehendes Metadaten-File updaten, indem das Alte gelöscht wird und das Geänderte gespeichert wird.
     * @param metadata          Metadaten-Objekt zum Speichern.
     * @param oldMetadataID     Die Id des alten Metadaten-File, das ersetzt wird.
     * @param oldSaveLocation   Der Ort, an dem das zu-Updatende File gespeichert ist.
     */
    @Override
    public void update(Metadata metadata, String oldMetadataID, String oldSaveLocation)
    {
        if (delete(oldMetadataID, oldSaveLocation))
        {
            save(metadata);
        }
    }

    /**
     * Ausgewähltes Metadaten-File aus der DB löschen.
     * @param metadataID    Id des Metadaten-Files, das gelöscht werden soll.
     * @param saveLocation  Der Ort, an dem das zu-Löschende File gespeichert ist.
     * @return              → true, wenn die Löschung geklappt hat.
     *                      → false, wenn die Löschung nicht geklappt hat.
     */
    @Override
    public boolean delete(String metadataID, String saveLocation)
    {
        if (saveLocation.equals(this.dataSetName))
        {
            collectionDataset1.deleteMany(new Document("metadataID", metadataID));
            return true;
        } else {
            return false;
        }
    }
}
