package project.simtv_mam_app.gui_controller;

import project.simtv_mam_app.dataRessource.Metadata;
import project.simtv_mam_app.gui_model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static project.simtv_mam_app.Messages.*;

/**
 * Der Controller der Startseite.
 * @author Simon Wittker
 */
public class StartPageController implements Initializable
{
    @FXML
    private Button closeButton;
    @FXML
    private ChoiceBox<String> attributeChoiceBox;
    @FXML
    private Label dbDetailsLabel;
    @FXML
    private Label folderDetailsLabel;
    @FXML
    private Label searchInLocationDetailLabel;
    @FXML
    private Label searchAttributHintLabel;
    @FXML
    private RadioButton searchInDBRadioButton;
    @FXML
    private RadioButton searchInFolderRadioButton;
    @FXML
    private TextField searchInputField;

    private Model model;

    /**
     * Wird aufgerufen, um den Controller zu initialisieren.
     * @param url   Wird verwendet, um relative Pfade für das root-object zu regeln (kann auch null sein)
     * @param resourceBundle    Die Ressource, die beim lokalisieren des root-object verwendet wird (kann auch null sein)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        model = Model.getModelObj();
        String[] attributeList = Metadata.metadataAttributList();
        // Befüllen der ChoiceBox
        attributeChoiceBox.getItems().addAll(attributeList);
    }

    /**
     * Click-Handler für den "Suche starten"-Button.
     * → Erstellt zuerst ein String-Array mit Such-Attribut und Suchbegriff
     *   mit der Methode "createSearchTermAndAttributList()".
     * → Erweitert diesen mit dem Ort der Durchsuchung in der Methode "createSearchStringArray()".
     * → Dann startet er die Suche mit der "startSearching"-Methode
     * @param event Ein Mausklick auf den Button "Suche starten".
     */
    @FXML
    public void clickOnSearchButton(ActionEvent event)
    {
        String[] searchTermAndAttribute = createSearchTermAndAttributList();
        String[] searchStringArray = createSearchStringArray(searchTermAndAttribute);
        startSearching(searchStringArray, event);
    }

    /**
     * Methode zur Erstellung des String-Arrays mit dem Such-Attribut und dem Suchbegriff.
     * Speichert das Such-Attribut im Model.
     * Und überprüft mit der Model-Methode "searchTermAndAttributCheck()", ob die beiden gültig sind.
     * @return  → Wenn ein Such-Attribut gewählt wurde, dann ein gültiges String-Array mit Such-Attribut und Suchbegriff.
     *          → Wenn kein Such-Attribut gewählt wurde, dann kommt ein "null"-Element zurück plus Fehlermeldung.
     */
    @FXML
    private String[] createSearchTermAndAttributList()
    {
        String searchTerm = searchInputField.getText();
        String searchAttribute = attributeChoiceBox.getValue();

        if (searchAttribute != null)
        {
            model.setValueAttributeChoiceBox(searchAttribute);
            searchAttributHintLabel.setVisible(false);
            return model.searchTermAndAttributCheck(searchAttribute, searchTerm);
        } else {
            searchAttributHintLabel.setVisible(true);
            noSearchAttributeError();
            return null;
        }
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
    private String[] createSearchStringArray(String[] searchTermAndAttributList)
    {
        String searchLocation = searchInLocationDetailLabel.getText();
        if (searchLocation.isEmpty())
        {
            missingSearchLocationChoiceError();
            return null;
        }
            return new String[]{searchTermAndAttributList[0], searchTermAndAttributList[1], searchLocation};
    }

    /**
     * Startet die Suche und öffnet das Such-Resultaten-Fenster.
     * @param event  Der Mausklick auf "Suche starten."
     * @param searchStringArray Ein gültiges String-Array mit Such-Attribut, Suchbegriff und einem Durchsuchungsort,
     *                          oder ein "null"-Element, wenn kein Such-Attribut ausgewählt wurde.
     */
    private void startSearching(String[] searchStringArray, ActionEvent event)
    {
        // null-Element wird abgefangen.
        if (searchStringArray != null)
        {
            model.searchMetadata(searchStringArray, model.getStrategyVariable());
            try
            {
                model.openSearchResultWindow(event);
            } catch (IOException e) {
                e.printStackTrace();
                failedViewLoadError();
            }
        }
    }

    /**
     * Click-Handler für den "searchInDB"-RadioButton
     */
    @FXML
    public void clickOnSearchInDBRadioButton()
    {
        model.dbRadioButtonHandler(searchInDBRadioButton, dbDetailsLabel, searchInLocationDetailLabel,
                searchInFolderRadioButton, folderDetailsLabel);
    }


    /**
     * Click-Handler für den "searchInFolder"-RadioButton
     */
    @FXML
    public void clickOnSearchInFolderRadioButton()
    {
        model.folderRadioButtonHandler(searchInFolderRadioButton, folderDetailsLabel,
                searchInLocationDetailLabel, searchInDBRadioButton, dbDetailsLabel);
    }


    /**
     * ClickHandler für den "neue Daten hinzufügen"-Button.
     * Öffnet dadurch die Seite mit der Eingabemaske und händelt die Exception.
     * @param event ein Mausklick auf den "neue Daten hinzufügen"-Button startet die Methode.
     */
    @FXML
    public void clickOnAddNewDataButton(ActionEvent event)
    {
            // Boolean wird gesetzt, für die Configuration der Datenmaske.
            model.setCreateNewMetadata(true);
            try
            {
                model.openDataInputMaskWindow(event);
            } catch (IOException e)
            {
                e.printStackTrace();
                failedViewLoadError();
            }
    }

    /**
     * Schliesst die Applikation, wenn auf den "Programm beenden"-Button geklickt wird.
     */
    @FXML
    public void clickOnCloseButton()
    {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }
}
