package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.*;

import java.util.List;

/**
 * Interface-Klasse Strategy für das strategy-Pattern .
 * @author Simon Wittker
 */
public interface Strategy
{
    /**
     * Metadaten in ein File speichern.
     * @param metadata Metadaten-Objekt zum Speichern.
     */
    void save(Metadata metadata);

    /**
     * Metadaten-File suchen.
     * @param searchStringArray       Such-Attribut, Suchbegriff und der Ort, der durchsucht werden soll.
     * @return                        Eine Liste mit einem String, der die Daten der Treffer beinhaltet.
     */
    List<String> search(String[] searchStringArray);

    /**
     * Metadaten-File updaten.
     * @param metadata          Metadaten-Objekt zum Speichern.
     * @param oldMetadataID     Die Id des alten Metadaten-File, das ersetzt wird.
     * @param oldSaveLocation      Der Ort, an dem das zu-Updatende File gespeichert ist.
     */
    void update(Metadata metadata, String oldMetadataID, String oldSaveLocation);

    /**
     * Metadaten-File löschen.
     * @param metadataID    Id des Metadaten-Files, das gelöscht werden soll.
     * @param saveLocation  Der Ort, an dem das zu-Löschende File gespeichert ist.
     * @return              → true, wenn die Löschung geklappt hat.
     *                      → false, wenn die Löschung nicht geklappt hat.
     */
    boolean delete(String metadataID, String saveLocation);
}