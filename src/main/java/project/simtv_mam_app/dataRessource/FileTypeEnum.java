package project.simtv_mam_app.dataRessource;

/**
 * Diese Enum-Klasse enthält die einzelnen File-Typen.
 */
public enum FileTypeEnum
{
    /**
     * Die einzelnen File-Typen-Objekte.
     */
    UNKNOWN("Typ: Unbekannter File-Typ."),
    TEXT("Typ: reine Textdatei, z.B. Zeitungsartikel, Blog oder Leserbrief (etc.) ohne Bilder"),
    TEXT_AND_PICTURE ("Typ: Textdatei mit Bilder, z.B. Zeitungsartikel oder Blog (etc.) mit Bilder"),
    PICTURE("Typ: Bilderdatei, z.B. Poster, Foto, Bild"),
    AUDIO_PODCAST("Typ: Audiodatei mit Sprachaufnahme ohne Musik, z.B. Podcast"),
    AUDIO_MUSIC("Typ: Audiodatei mit Musik/Gesang, z.B. Musikstück"),
    VIDEO_SERIE("Typ: Videodatei, z.B. Folge einer Serie"),
    VIDEO_MOVIE("Typ: Videodatei, z.B. Spielfilm"),
    VIDEO_BROADCAST("Typ: Videodatei, z.B. Sendung");

    String description;

    /**
     * Der Konstruktor
     * @param description Die Beschreibung.
     */
    FileTypeEnum(String description)
    {
        this.description = description;
    }

    /**
     * Diese Methode erstellt eine Liste mit den Namen der einzelnen File-Typen für die ChoiceBox.
     * @return  Ein String-Array mit den Namen der File-Typen.
     */
    public static String[] getAllTypeList()
    {
       String[] fileTypeList = new String[9];

       fileTypeList[0] = "Unbekannt";
       fileTypeList[1] = "Text";
       fileTypeList[2] = "Text mit Bild(er)";
       fileTypeList[3] = "Bild";
       fileTypeList[4] = "Audio (Podcast)";
       fileTypeList[5] = "Audio (Musik)";
       fileTypeList[6] = "Video (Serie)";
       fileTypeList[7] = "Video (Film)";
       fileTypeList[8] = "Video (Sendung)";

       return fileTypeList;
    }

    /**
     * Methode zur Übersetzung der deutschen Typenbezeichnung ins Englische.
     * @param type  Die deutsche Typenbeschreibung.
     * @return      Die englische Typenbeschreibung.
     */
    public static String translateTypeDEtoE(String type)
    {
        switch (type)
        {
            case "Unbekannt" -> { return "UNKNOWN"; }
            case "Text" -> { return "TEXT"; }
            case "Text mit Bild(er)" -> { return "TEXT_AND_PICTURE"; }
            case "Bild" -> { return "PICTURE"; }
            case "Audio (Podcast)" -> { return "AUDIO_PODCAST"; }
            case "Audio (Musik)" -> { return "AUDIO_MUSIC"; }
            case "Video (Serie)" -> { return "VIDEO_SERIE"; }
            case "Video (Film)" -> { return "VIDEO_MOVIE"; }
            case "Video (Sendung)" -> { return "VIDEO_BROADCAST"; }
            default -> { return "Type nicht gefunden bei DE zu E"; }
        }
    }

    /**
     * Methode zur Übersetzung der englischen Typenbezeichnung ins Deutsche.
     * @param fileTypeString Die englische Typenbeschreibung.
     * @return               Die deutsche Typenbeschreibung.
     */
    public static String translateTypeEtoDE(String fileTypeString)
    {
        switch (fileTypeString)
        {
            case "UNKNOWN" -> { return "Unbekannt"; }
            case "TEXT" -> { return "Text"; }
            case "TEXT_AND_PICTURE" -> { return "Text mit Bild(er)"; }
            case "PICTURE" -> { return "Bild"; }
            case "AUDIO_PODCAST" -> { return "Audio (Podcast)"; }
            case "AUDIO_MUSIC" -> { return "Audio (Musik)"; }
            case "VIDEO_SERIE" -> { return "Video (Serie)"; }
            case "VIDEO_MOVIE" -> { return "Video (Film)"; }
            case "VIDEO_BROADCAST" -> { return "Video (Sendung)"; }
            default -> { return "Fehler -> Typ nicht gefunden bei E zu DE"; }
        }
    }
}
