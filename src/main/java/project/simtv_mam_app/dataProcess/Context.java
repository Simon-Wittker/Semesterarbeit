package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.*;

import java.util.List;

import static project.simtv_mam_app.Messages.wrongStrategyChoseError;


/**
 * Context-Klasse vom strategy-Pattern.
 * Zuständig für die richtige Wahl der Strategie.
 * @author Simon Wittker
 */
public class Context
{
    private Strategy strategy;

    // Setter
    public void setStrategyDB(String saveLocation) { strategy = new StrategyDB(saveLocation); }
    public void setStrategyFolder() { strategy = new StrategyFolder(); }


    /**
     * Methode, um das Metadaten-Objekt mit der gewählten Strategie zu Speichern.
     * @param metadata      Das zu-Speichernde Metadaten-Objekt.
     * @param strategyVar   Die Strategie-Variable, die besagt, welche Strategie benutzt werden soll.
     */
    public void saveStrategy(Metadata metadata, int strategyVar)
    {
        switch(strategyVar)
        {
            case 1 -> setStrategyDB(metadata.getSaveLocation());
            case 2 -> setStrategyFolder();
            default -> wrongStrategyChoseError();
        }
        strategy.save(metadata);
    }

    /**
     * Methode, um nach einem Metadaten-File mit der gewählten Strategie zu suchen.
     * @param searchStringArray        Such-Attribut, Suchbegriff und der Ort, der durchsucht werden soll.
     * @param strategyVar              Die Strategie-Variable, die besagt, welche Strategie benutzt werden soll.
     * @return                         Eine Liste mit einem String, der die Daten der Treffer beinhaltet.
     */
    public List<String> searchStrategy(String[] searchStringArray, int strategyVar)
    {
        switch(strategyVar)
        {
            case 1 -> setStrategyDB(searchStringArray[2]);
            case 2 -> setStrategyFolder();
            default -> wrongStrategyChoseError();
        }
        return strategy.search(searchStringArray);
    }

    /**
     * Methode, um ein bestehendes Metadaten-File mit der gewählten Strategie upzudaten.
     * @param metadata          Das upgedatete Metadaten-Objekt.
     * @param oldMetadataID     Die ID des alten Metadaten-Objekts, das ersetzt werden soll.
     * @param oldSaveLocation   Der Ort, an dem das zu-Löschende File gespeichert ist.
     * @param strategyVar       Die Strategie-Variable, die besagt, welche Strategie benutzt werden soll.
     */
    public void updateStrategy(Metadata metadata, String oldMetadataID, String oldSaveLocation, int strategyVar)
    {
        switch(strategyVar)
        {
            case 1 -> setStrategyDB(metadata.getSaveLocation());
            case 2 -> setStrategyFolder();
            default -> wrongStrategyChoseError();
        }
        strategy.update(metadata, oldMetadataID, oldSaveLocation);
    }

    /**
     * Methode, um ein bestehendes Metadaten-File mit der gewählten Strategie zu löschen.
     * @param metadata          Das Metadaten-Objekt, dessen File gelöscht werden soll.
     * @param strategyVar       Die Strategie-Variable, die besagt, welche Strategie benutzt werden soll.
     * @return                  → true, wenn die Löschung geklappt hat.
     *                          → false, wenn die Löschung nicht geklappt hat.
     */
    public boolean deleteStrategy(Metadata metadata, int strategyVar)
    {
        switch(strategyVar)
        {
            case 1 -> setStrategyDB(metadata.getSaveLocation());
            case 2 -> setStrategyFolder();
            default -> wrongStrategyChoseError();
        }
        return strategy.delete(metadata.getMetadataID(), metadata.getSaveLocation());
    }
}
