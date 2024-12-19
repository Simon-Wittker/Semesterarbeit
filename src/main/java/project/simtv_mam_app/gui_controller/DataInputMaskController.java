package project.simtv_mam_app.gui_controller;

import project.simtv_mam_app.dataRessource.*;
import project.simtv_mam_app.gui_model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static project.simtv_mam_app.Messages.*;


/**
 * Der Controller der DatenMasken-Seite.
 * @author Simon Wittker
 */
public class DataInputMaskController implements Initializable
{
    @FXML
    private Button deleteButton;
    @FXML
    private ChoiceBox<String> fileTypeChoiceBox;
    @FXML
    private DatePicker fileDatePicker;
    @FXML
    private Label saveDBDetailsLabel;
    @FXML
    private Label saveFolderDetailsLabel;
    @FXML
    private Label metadataIDLabel;
    @FXML
    private Label metadataIDValueLabel;
    @FXML
    private Label missingIDInputLabel;
    @FXML
    private Label missingTitleInputLabel;
    @FXML
    private Label saveLocationDetail;
    @FXML
    private RadioButton bytesRadioButton;
    @FXML
    private RadioButton kiloBytesRadioButton;
    @FXML
    private RadioButton megaBytesRadioButton;
    @FXML
    private RadioButton gigaBytesRadioButton;
    @FXML
    private RadioButton saveDBRadioButton;
    @FXML
    private RadioButton saveFolderRadioButton;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextArea fileCastTextArea;
    @FXML
    private TextField fileAuthorTextField;
    @FXML
    private TextField fileIDTextField;
    @FXML
    private TextField fileSizeTextField;
    @FXML
    private TextField fileTitleTextField;

    /**
     * für die Überprüfung der Inputs der Datenmaske.
     */
    private boolean stringOK = true;
    private boolean numberOK = true;
    private boolean dateOK = true;
    private boolean descriptionOK = true;
    private boolean saveLocationOK = true;
    private boolean dataReadyToSave = false;

    /**
     * Lokaler Speicher für den Wert der RadioButton.
     */
    private int fileSizeUnit;

    private Model model;

    /**
     * Wird aufgerufen, um den Controller zu initialisieren.
     * Plus wird die ChoiceBox mit Daten befüllt.
     * @param url             Wird verwendet, um relative Pfade für das root-object zu regeln (kann auch null sein).
     * @param resourceBundle  Die Ressource, die beim lokalisieren des root-object verwendet wird (kann auch null sein).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        model = Model.getModelObj();
        String[] fileTypeList = FileTypeEnum.getAllTypeList();
        // Befüllen der ChoiceBox
        fileTypeChoiceBox.getItems().addAll(fileTypeList);

        if (model.getCreateNewMetadata())
        {
            deleteButton.setVisible(false);
            metadataIDLabel.setVisible(false);
        } else {
            deleteButton.setVisible(true);
            metadataIDLabel.setVisible(true);
            // befüllt die Elemente der Seite mit einem Suchresultat
            fillDataInDataMask();
        }
    }

    /**
     * Diese Methode bezieht alle Informationen aus der Datenmaske und liefert sie an das Model weiter,
     * das damit ein neues Metadaten-Objekt erstellt.
     */
    @FXML
    private void getDataFromDataMaskAndSendToModel()
    {
        // Übergangs-Variablen zum Gültigkeit-check
        String fileID = fileIDTextField.getText().trim();
        String fileTitle = fileTitleTextField.getText().trim();

        if (fileIDAndTitleCheck(fileID, fileTitle))
        {
            String metadataID;
            if (model.getCreateNewMetadata())
            {
                // neue Metadaten-Files bekommen im Model eine "metadataID"
                metadataID = "";
            } else {
                // Suchresultate haben bereits eine "metadataID"
                metadataID = metadataIDValueLabel.getText();
            }
            // Übergangs-Variablen mit Gültigkeit-Check
            double fileSizeInput = fileSizeCheckAndConverter(fileSizeTextField.getText());
            String fileAutor = stringInputCheck(fileAuthorTextField.getText());
            String fileCast = stringInputCheck(fileCastTextArea.getText());
            LocalDate fileDate = getDateFromDatePicker(fileDatePicker);
            String description = descriptionCheck(descriptionTextArea.getText());
            String saveLocation = saveLocationCheck(saveLocationDetail.getText());

            if (numberOK && stringOK && dateOK && descriptionOK && saveLocationOK)
            {
                dataReadyToSave = true;
                // Alle Informationen werden ans Model Übergeben
                model.createATemporaryMetadata(metadataID, fileID, fileTitle,
                        fileTypeChoiceBox.getValue(), fileSizeInput, fileSizeUnit, fileDate, fileAutor,
                        fileCast, description, saveLocation);
            }
        }
    }

    /**
     * RadioButton-Logik, dass nur einer ausgewählt wird, umfasst die nächsten vier Methoden.
     */
    @FXML
    public void clickOnBytesRadioButton()
    {
        fileSizeUnitRadioButtonHandler(bytesRadioButton, kiloBytesRadioButton,
                megaBytesRadioButton, gigaBytesRadioButton);
        fileSizeUnit = 0;
    }
    @FXML
    public void clickOnKiloBytesRadioButton()
    {
        fileSizeUnitRadioButtonHandler(kiloBytesRadioButton, bytesRadioButton,
                megaBytesRadioButton, gigaBytesRadioButton);
        fileSizeUnit = 1;
    }
    @FXML
    public void clickOnMegaBytesRadioButton()
    {
        fileSizeUnitRadioButtonHandler(megaBytesRadioButton, bytesRadioButton,
                kiloBytesRadioButton, gigaBytesRadioButton);
        fileSizeUnit = 2;
    }
    @FXML
    public void clickOnGigaBytesRadioButton()
    {
        fileSizeUnitRadioButtonHandler(gigaBytesRadioButton, bytesRadioButton,
                kiloBytesRadioButton, megaBytesRadioButton);
        fileSizeUnit = 3;
    }

    /**
     * Diese Hilfsmethode ist für die Logik der vier RadioButton zuständig.
     * Wenn der "radioButton1" ausgewählt wird, dann werden die anderen drei auf nicht-ausgewählt gesetzt.
     * @param radioButton1  Der RadioButton, der geklickt wurde.
     * @param radioButton2  Einer der anderen Radiobutton, die zurückgesetzt werden müssen.
     * @param radioButton3  Einer der anderen Radiobutton, die zurückgesetzt werden müssen.
     * @param radioButton4  Einer der anderen Radiobutton, die zurückgesetzt werden müssen.
     */
    private void fileSizeUnitRadioButtonHandler(RadioButton radioButton1, RadioButton radioButton2,
                                                RadioButton radioButton3, RadioButton radioButton4)
    {
        if (radioButton1.isSelected())
        {
            radioButton2.setSelected(false);
            radioButton3.setSelected(false);
            radioButton4.setSelected(false);
        } else {
            radioButton1.setSelected(false);
        }
    }

    /**
     * Click-Handler für die Speicherort-Wahl "DB".
     */
    @FXML
    public void clickOnSaveDBRadioButton()
    {
        model.dbRadioButtonHandler(saveDBRadioButton, saveDBDetailsLabel, saveLocationDetail,
                saveFolderRadioButton, saveFolderDetailsLabel);

        // Suchresultate mit Speicherorts-Änderung
        if (!model.getCreateNewMetadata())
        {
            final Optional<ButtonType> confirmForExit = confirmationSaveLocationChance();

            if (confirmForExit.isPresent() && confirmForExit.get() == ButtonType.CANCEL)
            {
                // Wenn auf "ABBRECHEN" geklickt wurde...
                deleteButton.setVisible(false);
                saveDBRadioButton.setSelected(false);
                saveDBDetailsLabel.setVisible(false);
                saveFolderRadioButton.setSelected(true);
                saveFolderDetailsLabel.setVisible(true);
                saveLocationDetail.setText(model.getFolderFilePath());
                model.setStrategyVariable(2);
            } else {
                // Wenn auf "OK" geklickt wurde...
                deleteButton.setVisible(false);

            }
        }
    }

    /**
     * Click-Handler für die Speicherort-Wahl "lokaler Ordner".
     */
    @FXML
    public void clickOnSaveFolderRadioButton()
    {
        model.folderRadioButtonHandler(saveFolderRadioButton, saveFolderDetailsLabel, saveLocationDetail,
                saveDBRadioButton, saveDBDetailsLabel);

        // Suchresultate mit Speicherorts-Änderung
        if (!model.getCreateNewMetadata())
        {
            final Optional<ButtonType> confirmForExit = confirmationSaveLocationChance();

            if (confirmForExit.isPresent() && confirmForExit.get() == ButtonType.CANCEL)
            {
                // Wenn auf "ABBRECHEN" geklickt wurde...
                saveFolderRadioButton.setSelected(false);
                saveFolderDetailsLabel.setVisible(false);
                saveDBRadioButton.setSelected(true);
                saveDBDetailsLabel.setVisible(true);
                saveLocationDetail.setText(model.getDataSetName());
                model.setStrategyVariable(1);
            } else {
                // Wenn auf "OK" geklickt wurde...
                deleteButton.setVisible(false);
            }
        }
    }

    /**
     * Testet den Input von FileID und FileTitle (ob er leer ist oder nicht).
     * Wenn leer, dann Fehlermeldung.
     * @param fileID     Input des fIleIDTextField.
     * @param fileTitel  Input des fileTitleTextField.
     * @return           → true, wenn die TextFelder ausgefüllt sind.
     *                   → false, wenn sie leer sind (Eine Fehlermeldung wird angezeigt).
     */
    private boolean fileIDAndTitleCheck(String fileID, String fileTitel)
    {
        if (!fileID.equals("") && !(fileTitel.equals("")))
        {
            if (!model.regexDelimiter(fileID) && !model.regexDelimiter(fileTitel))
            {
                return true;
            } else {
                wrongDelimiterError();
                return false;
            }
        } else {
            missingIDInputLabel.setText("**");
            missingTitleInputLabel.setText("**");
            missingIDInputLabel.setStyle("-fx-text-fill: red; -fx-font-size: 24");
            missingTitleInputLabel.setStyle("-fx-text-fill: red; -fx-font-size: 24");

            missingDataInputError();
            return false;
        }
    }

    /**
     * Testet die File-Grössen-Eingabe in das FileSizeTextField, ob es sich um Zahlen handelt.
     * @param fileSizeInput Der Input von fileSizeTextField.
     * @return            → Wenn der Input eine gültige Zahl ist: die Eingabe; dataOK auf true.
     *                    → Wenn keinen Input gemacht wurde: fileSize = 0.1; dataOK auf true.
     *                    → Wenn der Input ungültig ist: Eine Fehlermeldung wird angezeigt; dataOk auf false.
     */
    private double fileSizeCheckAndConverter(String fileSizeInput)
    {
        double doubleInput = 0.1;
        if (!fileSizeInput.isEmpty())
        {
            doubleInput = model.numberCheck(fileSizeInput);
            numberOK = !(doubleInput < 0);
        }
        return doubleInput;
    }

    /**
     * Testet den String-Input auf Buchstaben und Trennzeichen.
     * @param string      Input aus einem TextField (z.B. Autor oder Cast).
     * @return            → Wenn der Input keine Zahlen beinhaltet: der Input; dataOK auf true.
     *                    → Wenn kein Input gemacht wurde: String = ""; dataOK auf true.
     *                    → Wenn der Input ungültig ist: Eine Fehlermeldung wird angezeigt; dataOK auf false.
     */
    private String stringInputCheck(String string) {
        String defaultString = "";
        if (!string.isEmpty()) {
            stringOK = model.stringCheck(string);
            defaultString = string;
        }
        return defaultString;
    }

    /**
     * Testet die Datums-Eingabe, ob es ein Datum ist.
     * @param datePicker    Der DatenPicker von der Datenmaske.
     * @return            → Wenn der Input ein gültiges Datum ist: der Input; dataOK auf true.
     *                    → Wenn kein Input gemacht wurde: localDate = 1970-1-2; dataOK auf true.
     *                    → Wenn der Input ungültig ist: Eine Fehlermeldung wird angezeigt; dataOK auf false.
     */
    private LocalDate getDateFromDatePicker(DatePicker datePicker)
    {
        LocalDate localDate = LocalDate.of(1970,1,2);
        // Die Eingabe
        String fieldString = datePicker.getEditor().getText();

        if (fieldString.isEmpty()){
            dateOK = true;
            return localDate;
        } else if (model.regexTestDate(fieldString))
        {
            localDate = model.fromStringToDateAndCheck(fieldString);
            dateOK = localDate != null;
        } else {
            dateOK = false;
            wrongDatePickerInputError();
        }
        return localDate;
    }

    /**
     * Methode, zum Überprüfen, ob die Beschreibung die verbotenen Trennzeichen "," oder ":" enthält.
     * @param descriptionInput  Der Inhalt der TextArea.
     * @return                  → Wenn die Zeichen nicht vorhanden sind: gibt den Beschreibung-String zurück.
     *                          → Wenn ein verbotenes Zeichen gefunden wurde: Fehlermeldung.
     */
    private String descriptionCheck(String descriptionInput)
    {
        if (!model.regexDelimiter(descriptionInput))
        {
            descriptionOK = true;
            return descriptionInput;
        } else {
            wrongDelimiterError();
            descriptionOK = false;
            return "";
        }
    }

    /**
     * Methode zum Testen, ob ein Speicherort angewählt wurde oder nicht.
     * @param saveLocation  String-Input von dem Label, das angezeigt wird,
     *                      wenn auf einen der beiden Speicherort-RadioButton geklickt wurde.
     * @return             → Wenn ein Speicherort ausgewählt worden ist: gibt den Speicherort-String zurück.
     *                     → Wenn nicht: Fehlermeldung.
     */
    private String saveLocationCheck(String saveLocation)
    {
        String saveLocationDefault = model.getDataSetName();
        if (!saveLocation.isEmpty())
        {
            saveLocationOK = true;
            saveLocationDefault = saveLocation;
        } else {
            missingSaveLocationError();
            saveLocationOK = false;
        }
        return saveLocationDefault;
    }

    /**
     * Speicher-Methode:
     * 1. werden alle Informationen ausgelesen, geprüft und an das Model geschickt, mit der Methode "extractMetadata()".
     * 2. wird abgefragt, ist es ein neuer Datensatz, dann wird er neu erstellt mit "saveData()".
     * ... ist es ein Suchresultat, das bearbeitet wurde, dann wird der Datensatz ge-updated mit "saveUpdateData()".
     * @param event         ein Mausklick auf "Daten speichern."
     */
    @FXML
    public void clickOnSaveButton(ActionEvent event)
    {
        getDataFromDataMaskAndSendToModel();
        if (dataReadyToSave)
        {
            if (model.getCreateNewMetadata())
            {
                // neuer Datensatz wird erstellt
                model.saveMetadata();
                successfulSaveInfo();
                try
                {
                    model.openStartWindow(event);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    failedViewLoadError();
                }
            } else {
                // Bestehender Datensatz
                model.updateMetadata();
                successfulSaveInfo();

                model.searchMetadata(
                        new String[]{ model.getSearchAttribute(), model.getSearchTerm(),
                                model.getOldSaveLocation() }, model.getStrategyVariable());
                try
                {
                    model.openSearchResultWindow(event);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    failedViewLoadError();
                }
            }
            dataReadyToSave = false;
        }
    }

    /**
     * Methode zum Befüllen der Elemente, wenn ein Suchresultat bearbeitet werden soll.
     * Das Metadaten-Objekt wird vom Model geladen.
     */
    public void fillDataInDataMask()
    {
        Metadata m2 = model.getTemporaryMetadata();

        metadataIDValueLabel.setText(m2.getMetadataID());
        fileIDTextField.setText(m2.getFileID());
        fileTitleTextField.setText(m2.getFileTitle());
        fileTypeChoiceBox.setValue(FileTypeEnum.translateTypeEtoDE(m2.getFileType()));
        fileSizeTextField.setText(m2.getFileSize() + "");

        String fileSizeTypeString = m2.getFileSizeUnit();
        switch (fileSizeTypeString)
        {
            case "B" -> bytesRadioButton.setSelected(true);
            case "KB" -> kiloBytesRadioButton.setSelected(true);
            case "MB" -> megaBytesRadioButton.setSelected(true);
            case "GB" -> gigaBytesRadioButton.setSelected(true);
        }

        fileDatePicker.setValue(m2.getFileDate());
        fileAuthorTextField.setText(m2.getFileAuthor());
        fileCastTextArea.setText(m2.getFileCast());
        descriptionTextArea.setText(m2.getDescription());

        String saveLocationString = m2.getSaveLocation();
        if (model.getDataSetName().equals(saveLocationString))
        {
            saveDBRadioButton.setSelected(true);
            saveDBDetailsLabel.setVisible(true);
        } else
        {
            saveFolderRadioButton.setSelected(true);
            saveFolderDetailsLabel.setVisible(true);
        }
        saveLocationDetail.setText(saveLocationString);
    }

    /**
     * Methode zum Löschen eines bestehenden Datensatzes.
     * Öffnet zuerst ein Fragefenster, ob die Daten wirklich gelöscht werden sollen.
     * Wenn das Löschen bestätigt wurde, dann wird der Datensatz gelöscht.
     * Dann wird die letzte Suche noch einmal gestartet und das Such-Resultaten-Fenster geöffnet.
     * @param event Mausklick auf "Daten löschen".
     */
    public void clickOnDeleteButton(ActionEvent event)
    {
        final Optional<ButtonType> confirmForExit = confirmationDelete();

        if (confirmForExit.isPresent() && confirmForExit.get() == ButtonType.OK)
        {
            model.deleteMetadata();
            model.searchMetadata(new String[]{model.getSearchAttribute(), model.getSearchTerm(),
                    model.getOldSaveLocation()}, model.getStrategyVariable());
            try
            {
                model.openSearchResultWindow(event);
            } catch (IOException e)
            {
                e.printStackTrace();
                failedViewLoadError();
            }
        }
    }

    /**
    * Diese Methode öffnet die Startseite, ruft eine Methode der Start-Klasse auf.
    * Fragt nach, ob die Seite wirklich verlassen und die Daten nicht gespeichert werden sollen.
    * @param event        Mausklick auf "Zurück" oder "Abbrechen".
    */
    @FXML
    public void clickOnBackOrQuitButton(ActionEvent event)
    {
        final Optional<ButtonType> confirmForExit = confirmationMaskExit();
        if (confirmForExit.isPresent() && confirmForExit.get() == ButtonType.OK)
        {
            try
            {
                if (model.getCreateNewMetadata())
                {
                    model.openStartWindow(event);
                } else {
                    model.openSearchResultWindow(event);
                }
            } catch (IOException e )
            {
                e.printStackTrace();
                failedViewLoadError();
            }
        }
    }
}
