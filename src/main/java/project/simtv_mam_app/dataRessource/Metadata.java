package project.simtv_mam_app.dataRessource;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

/**
 * Diese Klasse definiert die Metadaten.
 * @author Simon Wittker
 */
public class Metadata implements Serializable
{
    /**
     * Alle Variablen, die die Metadaten enthalten.
     */
    private String metadataID;
    private String fileID;
    private String fileTitle;
    private FileTypeEnum fileType;
    private double fileSize;
    private FileSizeUnitEnum fileSizeUnit;
    private LocalDate fileDate;
    private String fileAuthor;
    private String fileCast;
    private String description;
    private String saveLocation;

    /**
     * Der Konstruktor ohne id für das Erstellen neuer Metadaten.
     * @param fileID    Die ID des Files, deren Metadaten gespeichert wird.
     * @param fileTitle Der Titel des Files, deren Metadaten gespeichert wird.
     */
    public Metadata(String fileID, String fileTitle) {
        this.fileID = fileID;
        this.fileTitle = fileTitle;
        fileType = FileTypeEnum.UNKNOWN;
        fileSize = 0.1;
        fileSizeUnit = FileSizeUnitEnum.B;
        fileDate = LocalDate.of(1970, 1, 1);
        fileAuthor = " ";
        fileCast = " ";
        description = "keine Beschreibung vorhanden.";
    }

    /**
     * Diese Methode kreiert die id beim Erstellen des Metadaten-Satzes.
     */
    public void createID() {
        Random random = new Random();
        int randomInt = random.nextInt(1000000);
        metadataID = fileID + "_" + fileTitle + "_" + randomInt;
    }

    /**
     * Diese Methode definiert die Ausgabe des Metadaten-Satzes in der Konsole.
     * @return Ausgabe-String für die Konsole.
     */
    public String toString() {
        if (metadataID == null) {
            return "Zuerst die ID erstellen!";
        } else {
            return "\n" +
                    "Metadaten_ID:     " + metadataID + "\n" +
                    "ID:               " + fileID + "\n" +
                    "Titel:            " + fileTitle + "\n" +
                    "Typ:              " + fileType.name() + "\n" +
                    "Grösse:           " + fileSize + " '" + fileSizeUnit.name() + "'\n" +
                    "Erstellungsdatum: " + fileDate + "\n" +
                    "Autor(en):        " + fileAuthor + "\n" +
                    "Besetzung:        " + fileCast + "\n" +
                    "Beschreibung:     " + description + "\n" +
                    "Speicherort:      " + saveLocation;

        }
    }

    /**
     * Methode zum Generieren eines Strings, der in dem lokalen Ordner gespeichert wird.
     * @return Der String, der gespeichert wird.
     */
    public String toStringForSaveInFolder()
    {
        return "{ { : }, \"metadataID\": \"" + metadataID+ "\", \"fileID\": \"" + fileID + "\"," +
                " \"fileTitle\": \"" + fileTitle + "\", \"fileType\": \"" + fileType + "\"," +
                " \"fileSize\": \""+ fileSize + "\", \"fileSizeUnit\": \"" + fileSizeUnit + "\", " +
                "\"fileDate\": \"" + fileDate + "\" , \"fileAuthor\": \"" + fileAuthor + "\"," +
                " \"fileCast\": \"" + fileCast + "\", \"description\": \"" + description + "\" ," +
                " \"saveLocation\": \"" + saveLocation + "\"}";
    }

    // Getter
    public String getMetadataID()
    {
        return Objects.requireNonNullElse(metadataID, "MetadataID nicht definiert.");
    }

    public String getFileID() { return fileID; }

    public String getFileTitle() { return fileTitle; }

    public String getFileType() { return fileType.name(); }

    public double getFileSize() { return fileSize; }

    public String getFileSizeUnit() { return fileSizeUnit.name(); }

    public LocalDate getFileDate() { return fileDate; }

    public String getFileAuthor() { return fileAuthor; }

    public String getFileCast() { return fileCast; }

    public String getDescription() { return description; }

    public String getSaveLocation() { return saveLocation; }

    // Setter
    public void setMetadataID(String id) { metadataID = id; }

    public void setFileID(String fileID) { this.fileID = fileID; }

    public void setFileTitle(String fileTitle) { this.fileTitle = fileTitle; }

    /**
     * Setter für dem fileType. Von den deutschen Begriffen zum File-Typ.
     * Kommt bei der ChoiceBox zum Einsatz.
     * @param typeName Die Bezeichnung des Types als String.
     */
    public void setFileType(String typeName)
    {
        switch (typeName)
        {
            case "Text" -> this.fileType = FileTypeEnum.TEXT;
            case "Text mit Bild(er)" -> this.fileType = FileTypeEnum.TEXT_AND_PICTURE;
            case "Bild" -> this.fileType = FileTypeEnum.PICTURE;
            case "Audio (Podcast)" -> this.fileType = FileTypeEnum.AUDIO_PODCAST;
            case "Audio (Musik)" -> this.fileType = FileTypeEnum.AUDIO_MUSIC;
            case "Video (Serie)" -> this.fileType = FileTypeEnum.VIDEO_SERIE;
            case "Video (Film)" -> this.fileType = FileTypeEnum.VIDEO_MOVIE;
            case "Video (Sendung)" -> this.fileType = FileTypeEnum.VIDEO_BROADCAST;
            default -> this.fileType = FileTypeEnum.UNKNOWN;
        }
    }

    /**
     * Setter für dem fileType. Von den englischen Begriffen zum File-Typ.
     * kommt bei der ChoiceBox zum Einsatz.
     * @param typeName Die Bezeichnung des Types als String
     */
    public void setFileTypeSearch(String typeName)
    {
        switch (typeName)
        {
            case "TEXT" -> this.fileType = FileTypeEnum.TEXT;
            case "TEXT_AND_PICTURE" -> this.fileType = FileTypeEnum.TEXT_AND_PICTURE;
            case "PICTURE" -> this.fileType = FileTypeEnum.PICTURE;
            case "AUDIO_PODCAST" -> this.fileType = FileTypeEnum.AUDIO_PODCAST;
            case "AUDIO_MUSIC" -> this.fileType = FileTypeEnum.AUDIO_MUSIC;
            case "VIDEO_SERIE" -> this.fileType = FileTypeEnum.VIDEO_SERIE;
            case "VIDEO_MOVIE" -> this.fileType = FileTypeEnum.VIDEO_MOVIE;
            case "VIDEO_BROADCAST" -> this.fileType = FileTypeEnum.VIDEO_BROADCAST;
            default -> this.fileType = FileTypeEnum.UNKNOWN;
        }
    }

    public void setFileSize(double newFileSize) { fileSize = newFileSize; }

    /**
     * Setter für fileSizeUnit. Zum Erstellen neuer Metadaten.
     * Kommt bei den RadioButton zum Einsatz.
     * @param value Integer-Wert, der für eine Grösse steht.
     */
    public void setFileSizeUnit(int value)
    {
        switch (value)
        {
            case 0 -> this.fileSizeUnit = FileSizeUnitEnum.B;
            case 1 -> this.fileSizeUnit = FileSizeUnitEnum.KB;
            case 2 -> this.fileSizeUnit = FileSizeUnitEnum.MB;
            case 3 -> this.fileSizeUnit = FileSizeUnitEnum.GB;
        }
    }

    /**
     * Setter für fileSizeUnit. Zum Bearbeiten bestehender Metadaten.
     * Kommt bei den RadioButton zum Einsatz.
     * @param value Integer-Wert, der für eine Grösse steht.
     */
    public void setFileSizeUnitSearch(String value) {
        switch (value) {
            case "B" -> this.fileSizeUnit = FileSizeUnitEnum.B;
            case "KB" -> this.fileSizeUnit = FileSizeUnitEnum.KB;
            case "MB" -> this.fileSizeUnit = FileSizeUnitEnum.MB;
            case "GB" -> this.fileSizeUnit = FileSizeUnitEnum.GB;
        }
    }

    public void setFileDate(LocalDate newFileDate) { fileDate = newFileDate; }

    public void setFileAuthor(String author) { fileAuthor = author; }

    public void setFileCast(String cast) { fileCast = cast; }

    public void setDescription(String describe) { this.description = describe; }

    public void setSaveLocation(String saveLocation) { this.saveLocation = saveLocation; }

    /**
     * Methode, zur Bereitstellung der Metadaten-Attribute for die ChoiceBox.
     * @return Ein String-Array mit den Attributen der Metadaten.
     */
    public static String[] metadataAttributList()
    {
        return new String[]{"Metadaten_ID","File-ID", "File-Titel", "File-Typ", "File-Grösse", "Grösseneinheit",
                "File-Erstellungsdatum", "File-Autor(en)", "File-Besetzung", "Metadaten-Beschreibung"};
    }
}
