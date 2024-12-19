package project.simtv_mam_app.gui_controller;

import project.simtv_mam_app.dataRessource.Metadata;
import project.simtv_mam_app.gui_model.Model;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static project.simtv_mam_app.Messages.*;

/**
 * Der Controller der Such-Resultat-Seite.
 * @author Simon Wittker
 */
public class SearchResultController implements Initializable
{
    @FXML
    private Button resultEditButton;
    @FXML
    private ChoiceBox<String> attributeChoiceBox;
    @FXML
    private Label dbDetailsLabel;
    @FXML
    private Label folderDetailsLabel;
    @FXML
    private Label newSearchInLocationDetailLabel;
    @FXML
    private ListView<String> resultListView  = new ListView<>();
    @FXML
    private RadioButton newSearchInDBRadioButton;
    @FXML
    private RadioButton newSearchInFolderRadioButton;
    @FXML
    private TextField newSearchInputField;

    private boolean itemInListViewer;

    private Model model;
    /**
     * Wird aufgerufen, um den Controller zu initialisieren. Befüllt die Elemente der Seite.
     * @param url            Wird verwendet, um relative Pfade für das root-object zu regeln (kann auch null sein).
     * @param resourceBundle Die Ressource, die beim lokalisieren des root-object verwendet wird (kann auch null sein).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        model = Model.getModelObj();
        String[] attributeList = Metadata.metadataAttributList();
        // Befüllen der ChoiceBox
        attributeChoiceBox.getItems().addAll(attributeList);
        // Befüllen der ListView mit den Suchresultaten
        fillSearchResultInListViewer(model.getResultObservableList());
        // Befüllen des Suchfeldes mit dem letzten Suchbegriff
        newSearchInputField.setText(model.getSearchTerm());
        //Befüllen der ChoiceBox mit dem zuletzt ausgewählten Attribut
        attributeChoiceBox.setValue(model.getValueAttributeChoiceBox());
        // Je nachdem, welchen Durchsuchungsort gewählt wurde, wird das jetzt wieder abgebildet
        if (model.getStrategyVariable() == 1)
        {
            newSearchInDBRadioButton.setSelected(true);
            dbDetailsLabel.setVisible(true);
            newSearchInLocationDetailLabel.setText(model.getDataSetName());
        }  else if (model.getStrategyVariable() == 2)
        {
            newSearchInFolderRadioButton.setSelected(true);
            folderDetailsLabel.setVisible(true);
            newSearchInLocationDetailLabel.setText(model.getFolderFilePath());
        }
    }

    /**
     * Click-Handler für den "Suche starten"-Button.
     * → Löscht zuerst die Einträge aus der ListView, die die Suchresultate anzeigt.
     * → Macht dann den "Suchresultate bearbeiten"-Button unsichtbar (Als Schutz vor NullPointerException).
     * → Erstellt dann ein String-Array mit Such-Attribut und Suchbegriff
     *   mit der Methode "createSearchTermAndAttributList()".
     * → Erweitert diesen mit dem Ort der Durchsuchung in der Methode "createSearchStringArray()".
     * → Dann startet er die Suche mit der "startNewSearching"-Methode.
     */
    @FXML
    public void clickOnNewSearchButton()
    {
        resultListView.getItems().clear();
        resultEditButton.setVisible(false);

        String[] searchTermAndAttribute = createNewSearchTermAndAttributList();
        String[] searchStringArray = createNewSearchStringArray(searchTermAndAttribute);

        startNewSearching(searchStringArray);
    }

    /**
     * Methode zur Erstellung des String-Arrays mit dem Such-Attribut und dem Suchbegriff.
     * Speichert das Such-Attribut und den Suchbegriff im Model.
     * Und überprüft mit der Model-Methode "searchTermAndAttributCheck()", ob die beiden gültig sind.
     * @return Ein gültiges String-Array mit Such-Attribut und Suchbegriff.
     */
    @FXML
    private String[] createNewSearchTermAndAttributList()
    {
        String searchTerm = newSearchInputField.getText();
        String searchAttribute = attributeChoiceBox.getValue();
        model.setValueAttributeChoiceBox(searchAttribute);

        return model.searchTermAndAttributCheck(searchAttribute, searchTerm);
    }

    /**
     * Erstellt ein dem String-Array aus Such-Attribut und Suchbegriff und dem Ort, der durchsucht werden soll.
     * Prüft ausserdem, ob dieser Ort ausgewählt wurde oder nicht.
     * @param searchTermAndAttributList Ein gültiges String-Array mit Such-Attribut und Suchbegriff,
     *                                  oder ein "null"-Element, wenn kein Such-Attribut ausgewählt wurde.
     * @return   → Ein String-Array mit Such-Attribut und Suchbegriff und dem Durchsuchungsort.
     *           → Ein "null"-Element, wenn keinen Ort zum Durchsuchen gewählt wurde.
     */
    @FXML
    private String[] createNewSearchStringArray(String[] searchTermAndAttributList)
    {
        String searchLocation = newSearchInLocationDetailLabel.getText();
        if (searchLocation.isEmpty())
        {
            missingSearchLocationChoiceError();
            return null;
        }
        if (searchTermAndAttributList != null)
        {
            return new String[]{searchTermAndAttributList[0], searchTermAndAttributList[1], searchLocation};
        } else {
            return null;
        }
    }

    /**
     * Startet eine neue Suche.
     * @param newSearchStringArray Ein gültiges String-Array mit Such-Attribut, Suchbegriff und einem Durchsuchungsort,
     *                             oder ein "null"-Element, wenn kein Such-Attribut ausgewählt wurde.
     */
    @FXML
    private void startNewSearching(String[] newSearchStringArray)
    {
        if (newSearchStringArray != null)
        {
            model.searchMetadata(newSearchStringArray, model.getStrategyVariable());

            // Suchresultate werden in den ListViewer geladen und angezeigt.
            fillSearchResultInListViewer(model.getResultObservableList());
        }
    }

    /**
     * Click-Handler für den "searchInDB"-RadioButton.
     */
    @FXML
    public void clickOnNewSearchInDBRadioButton()
    {
        model.dbRadioButtonHandler(newSearchInDBRadioButton, dbDetailsLabel,
                newSearchInLocationDetailLabel, newSearchInFolderRadioButton, folderDetailsLabel);
    }

    /**
     * Click-Handler für den "searchInFolder"-RadioButton.
     */
    @FXML
    public void clickOnNewSearchInFolderRadioButton()
    {
        model.folderRadioButtonHandler(newSearchInFolderRadioButton, folderDetailsLabel,
                newSearchInLocationDetailLabel, newSearchInDBRadioButton, dbDetailsLabel);
    }

    /**
     * Methode zum befüllen der ListView mit den Suchresultaten.
     * @param metadataObservableList    eine ObservableList mit den Suchresultaten.
     */
    @FXML
    public void fillSearchResultInListViewer(ObservableList<Metadata> metadataObservableList)
    {
        if (metadataObservableList.size() == 0)
        {
            itemInListViewer = false;
            resultListView.setPlaceholder(new Label("Keine Treffer gefunden!"));
        } else {
            itemInListViewer = true;
            metadataObservableList.forEach(metadata ->
                    resultListView.getItems().add(model.metadataToStringForResultView(metadata)));
        }
    }

    /**
     * Methode, die das angewählte Item des ListView ladet und dem Model übergibt.
     * Datenmasken-Seite wird geöffnet.
     * @param event         ausgelöst mit einem Mausklick auf den "Suchresultate bearbeiten"-Button.
     */
    @FXML
    public void clickOnResultEditButton(ActionEvent event)
    {
        String item = resultListView.getSelectionModel().getSelectedItem();
        if (item != null)
        {
            model.setSearchResultItem(model.itemToMetadata(item));
            model.setCreateNewMetadata(false);
            try
            {
                model.openDataInputMaskWindow(event);
            } catch (IOException e)
            {
                e.printStackTrace();
                failedViewLoadError();
            }
        }
    }

    /**
     * Methode, die den "Suchresultate bearbeiten"-Button sichtbar macht,
     * wenn auf ein Suchresultat in der Listview geklickt wurde.
     * Zeigt eine Fehlermeldung an, wenn kein Suchresultat in der ListView drin ist.
     */
    @FXML
    public void clickOnListView()
    {
        if (itemInListViewer)
        {
            resultEditButton.setVisible(true);
        } else {
            noItemInListViewerInfo();
        }
    }

    /**
     * Öffnet die Startseite.
     * @param event         ein Mausklick auf den "Zurück"-Button.
     * @throws IOException, wenn der FXML-Loader das fxml-File nicht findet.
     */
    @FXML
    public void clickOnBackButton(ActionEvent event) throws IOException
    {
        model.openStartWindow(event);
    }
}
