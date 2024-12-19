package project.simtv_mam_app.gui_model;

import project.simtv_mam_app.dataProcess.Context;
import project.simtv_mam_app.dataRessource.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import project.simtv_mam_app.Start;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static project.simtv_mam_app.Messages.*;

/**
 * Model für das gesamte Projekt.
 * Dient zur Speicherung der Daten sowie der Bereitstellung sämtlicher Hilfsfunktionen für die Controller.
 */
public class Model
{
    // wenn die Applikation erweitert wird, ist hier der einzige Speicherort von dataSetName und folderFilePath.
    private final String dataSetName = "DataSet1";
    private final String folderFilePath = "/Volumes/SIMTV MAM/Lokaler Ordner";

    private String searchTerm;
    private String searchAttribute;
    private String valueAttributeChoiceBox;

    private String oldMetadataID;
    private String oldSaveLocation;
    private Metadata temporaryMetadata;

    private int strategyVariable;
    private boolean createNewMetadata;

    private ObservableList<Metadata> resultObservableList;

    // Das Singleton-Objekt
    private static final Model MODEL = new Model();

    private final Context context  = new Context();

    /**
     * Methode zur Erstellung eines temporären Metadaten-Objektes.
     * Erstellt mit den Daten aus der Datenmaske, die vom Controller übergeben wurden.
     * @param metadataID   die ID des Metadaten-Satzes
     * @param fileID       die File-ID
     * @param fileTitle    der File-Titel
     * @param fileTypeName der File-Type
     * @param fileSize     die File-Grösse
     * @param fileSizeUnit die Einheit der File-Grösse
     * @param fileDate     das Erstellungsdatum des Files
     * @param fileAuthor   der Autor oder die Autoren des Files
     * @param fileCast     die Besetzung des Files
     * @param description  die Beschreibung der Metadaten
     * @param saveLocation der Speicherort
     *
     */
    public void createATemporaryMetadata(String metadataID, String fileID, String fileTitle, String fileTypeName,
                                         double fileSize, int fileSizeUnit, LocalDate fileDate,
                                         String fileAuthor, String fileCast, String description,
                                         String saveLocation)
    {
        // Die Initiierung des Metadaten-Objektes
        temporaryMetadata = new Metadata(fileID, fileTitle);

        if (metadataID.equals("") )
        {
            temporaryMetadata.createID();
        } else {
            oldMetadataID = metadataID;
            temporaryMetadata.setMetadataID(metadataIDCheck(temporaryMetadata, oldMetadataID));
        }

        if (fileTypeName != null)
        {
            temporaryMetadata.setFileType(fileTypeName);
        }
        temporaryMetadata.setFileSize(fileSize);
        temporaryMetadata.setFileSizeUnit(fileSizeUnit);
        temporaryMetadata.setFileDate(fileDate);
        temporaryMetadata.setFileAuthor(fileAuthor);
        temporaryMetadata.setFileCast(fileCast);

        if (!description.isEmpty())
        {
            temporaryMetadata.setDescription(description);
        }

        if (saveLocation.equals(dataSetName))
        {
            temporaryMetadata.setSaveLocation(saveLocation);
        } else {
            temporaryMetadata.setSaveLocation(folderFilePath + "/SaveFile_" + temporaryMetadata.getMetadataID() + "_.txt");
        }
    }


    /**
     * Methode zum Speicher von einem Metadaten-File.
     * Das zu speichernde Metadaten-Objekt ist im temporären Metadaten-Objekt gespeichert.
     * Mitgegeben wird die "strategyVariable", die anzeigt, wo gespeichert werden soll.
     */
    public void saveMetadata()
    {
        context.saveStrategy(temporaryMetadata, strategyVariable);
    }

    /**
     * Methode zum Updaten von einem Metadaten-File.
     * Das zu speichernde Metadaten-Objekt ist im temporären Metadaten-Objekt gespeichert.
     * Mitgegeben wird die "strategyVariable", die anzeigt, wo gespeichert werden soll.
     * Die "oldMetadataID" und die "oldSaveLocation" sind zum Löschen des aktuellen Speichersatzes.
     */
    public void updateMetadata()
    {
        // Beim Speicherort-Wechsel, von DB zum lokalen Ordner...
        if (oldSaveLocation.equals(dataSetName) &&
                temporaryMetadata.getSaveLocation().equals(folderFilePath + "/SaveFile_" +
                        temporaryMetadata.getMetadataID() + "_.txt"))
        {
            context.saveStrategy(temporaryMetadata,2);
            temporaryMetadata.setMetadataID(oldMetadataID);
            context.deleteStrategy(temporaryMetadata,1);
            strategyVariable = 1;
        // Beim Speicherort-Wechsel, vom lokalen Ordner zur DB...
        } else if ( oldSaveLocation.equals(folderFilePath) && temporaryMetadata.getSaveLocation().equals(dataSetName))
        {
            context.saveStrategy(temporaryMetadata,1);
            temporaryMetadata.setMetadataID(oldMetadataID);
            temporaryMetadata.setSaveLocation(oldSaveLocation);
            context.deleteStrategy(temporaryMetadata,2);
            strategyVariable = 2;
            // Update ohne Speicherort-Wechsel
        } else {
            context.updateStrategy(temporaryMetadata, oldMetadataID, oldSaveLocation, strategyVariable);
        }
    }

    /**
     * Methode zum Löschen von einem Metadaten-File.
     * Das zu löschende Metadaten-Objekt ist im temporären Metadaten-Objekt gespeichert.
     * Mitgegeben wird die "strategyVariable", die anzeigt, wo das Objekt gespeichert ist.
     */
    public void deleteMetadata()
    {
        if (context.deleteStrategy(temporaryMetadata, strategyVariable))
        {
            successfulDeletedInfo();
        } else {
            failedDeletedInfo();
        }
    }

    /**
     * Methode zum Überprüfen, ob, beim Bearbeiten eines Suchresultates, die File-ID/der File-Titel verändert wurde.
     * @param metadata   Das Metadaten-Objekt, dass vom Controller übergeben wurde.
     * @param metadataID Die Metadaten-ID, die vom Controller mitgeliefert wurde.
     * @return           → Die "alte" Metadata-ID wird zurückgegeben, wenn nichts verändert wurde.
     *                   → Eine neue Metadata-ID wird kreiert, wenn sich die File-ID/der File-Titel geändert hat.
     */
    protected String metadataIDCheck(Metadata metadata, String metadataID)
    {
        String[] metadataPieces = metadataID.split("[_]");
        if (metadataPieces[0].equals(metadata.getFileID()) && metadataPieces[1].equals(metadata.getFileTitle()))
        {
            return metadataID;
        } else {
            return metadata.getFileID() + "_" + metadata.getFileTitle() + "_" + metadataPieces[2];
        }
    }

    /**
     * Methode für das Testen und Umwandeln eines Strings, der Zahlen beinhaltet.
     * Getestet und umgewandelt wird die File-Grösse.
     * @param string    Ein String, der aus Zahlen bestehen soll.
     * @return          → Die Zahlen, die im String vorhanden waren, wenn wirklich nur Zahlen im String waren.
     *                  → -11, wenn nicht nur Zahlen im String waren + Fehlermeldung.
     */
    public double numberCheck(String string)
    {
        double d;
        try
        {
            d = Double.parseDouble(string);
        } catch (NumberFormatException e)
        {
            wrongFileSizeInputError();
            e.printStackTrace();
            return -11.0;
        }
        if (d < 0)
        {
            negativNumberError();
        }
        return d;
    }

    /**
     * Methode zum Testen von Strings, die keine Zahlen oder verbotene Trennzeichen beinhalten dürfen.
     * Getestet werden File-Autoren und File-Besetzung.
     * @param string    Ein Input eines TextFields, das keine Zahlen beinhalten darf.
     * @return          → true, wenn weder Zahlen noch verbotene Zeichen im String waren.
     *                  → false, wenn Zahlen oder verbotene Zeichen gefunden wurden.
     */
    public boolean stringCheck(String string)
    {
        for (char c : string.toCharArray())
        {
            if (Character.isDigit(c))
            {
                wrongStringInputError();
                return false;
            }
        }

        if (regexDelimiter(string))
        {
            wrongDelimiterError();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Methode zum Testen und Umwandeln von einem String, der ein Datum beinhalten soll.
     * Getestet und umgewandelt wird das Erstellungsdatum.
     * @param dateString    Ein String, der ein Datum enthalten soll.
     * @return              → Das Datum, dass im String war, wenn das Datum gültig war.
     *                      → Eine Fehlermeldung, wenn das Datum ungültig war + null-Objekt.
     */
    public LocalDate fromStringToDateAndCheck(String dateString)
    {
        LocalDate localDate;

        String[] stringArray = dateString.split("[.]");
        int day = Integer.parseInt(stringArray[0]);
        int month = Integer.parseInt(stringArray[1]);
        int year = Integer.parseInt(stringArray[2]);

        try
        {
            localDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e)
        {
            e.printStackTrace();
            wrongDateExceptionError();
            localDate = null;
        }
        return localDate;
    }

    /**
     * Methode zum RegEx-Testen von einem Datum-String.
     * @param dateString Ein Input-String, der ein Datum beinhalten soll.
     * @return           → true, wenn die Eingabe dem Pattern entspricht.
     *                   → false, wenn die Eingabe nicht dem Pattern entspricht.
     */
    public boolean regexTestDate(String dateString)
    {
        Pattern datePattern = Pattern.compile("[0-9][0-9][.][0-9][0-9][.][0-9][0-9][0-9][0-9]");
        Matcher matcher = datePattern.matcher(dateString);
        return matcher.find();
    }

    /**
     * Methode zum RegEx-Testen von einem String, der eine gültige Zahl enthalten soll.
     * Getestet wird die Eingabe der File-Grösse.
     * @param dateString    Input-String, der eine File-Grösse beinhalten soll.
     * @return              → true, wenn die Eingabe dem Pattern entspricht.
     *                      → false, wenn die Eingabe nicht dem Pattern entspricht.
     */
    public boolean regexFileSize(String dateString)
    {
        Pattern datePattern = Pattern.compile("^{1,6}[0-9]?[.]{1,6}[0-9]");
        Matcher matcher = datePattern.matcher(dateString);
        return matcher.find();
    }

    /**
     * Methode zum RegEx-Testen von einem String, der einen File-Typ beinhalten soll.
     * @param dateString  Input-String, der einen File-Typ beinhalten soll
     * @return            → true, wenn die Eingabe dem Pattern entspricht.
     *                    → false, wenn die Eingabe nicht dem Pattern entspricht.
     */
    public boolean regexFileSizeUnit(String dateString)
    {
        Pattern datePattern = Pattern.compile("^?[KMGkmg]?[Bb]$");
        Matcher matcher = datePattern.matcher(dateString);
        return matcher.find();
    }

    /**
     * Methode zum RegEx-Testen von einem String, ob er verbotene Trennzeichen beinhaltet.
     * @param inputString   Ein String, der überprüft werden soll, ob er Trennzeichen beinhaltet.
     * @return              → true, wenn Trennzeichen gefunden wurden.
     *                      → false, wenn keine Trennzeichen gefunden wurden.
     */
    public boolean regexDelimiter(String inputString)
    {
        Pattern datePattern = Pattern.compile("[,]|[:]|[\"]");
        Matcher matcher = datePattern.matcher(inputString);
        return matcher.find();
    }

    /**
     * Methode zur Überprüfung, ob der ausgewählte Suchbegriff zu dem ausgewählten Such-Attribut passt.
     * Geprüft wird je nachdem, welches Such-Attribut ausgewählt worden ist.
     * @param attribute     Das Such-Attribut
     * @param searchTerm    Der Suchbegriff
     * @return              → Ein gültiges Such-Attribut mit dem Suchbegriff, beides Strings.
     *                      → Eine aussagekräftige Fehlermeldung, wenn ein Suchbegriff nicht gültig ist.
     *                      → Ist das Attribut "null", dann wird eine Fehlermeldung und ein null-Objekt zurückgegeben.
     */
    public String[] searchTermAndAttributCheck(String attribute, String searchTerm)
    {
        if (attribute != null)
        {
            switch (attribute)
            {
                case "Metadaten_ID" -> {
                    if (!searchTerm.equals(""))
                    {
                        if (!regexDelimiter(searchTerm))
                        {
                        return new String[]{"metadataID", searchTerm};
                        } else {
                            wrongDelimiterError();
                        }
                    } else {
                        noSearchTermError();
                    }
                }
                case "File-ID" -> {
                    if (!searchTerm.equals(""))
                    {
                        if (!regexDelimiter(searchTerm))
                        {
                            return new String[]{"fileID", searchTerm};
                        } else {
                            wrongDelimiterError();
                        }
                    } else {
                        noSearchTermError();
                    }
                }
                case "File-Titel" -> {
                    if (!searchTerm.equals(""))
                    {
                        if (!regexDelimiter(searchTerm))
                        {
                            return new String[]{"fileTitle", searchTerm};
                        } else {
                            wrongDelimiterError();
                        }
                    } else {
                        noSearchTermError();
                    }
                }
                case "File-Typ" -> {
                    String[] fileTypeList = FileTypeEnum.getAllTypeList();
                    for (String type : fileTypeList)
                    {
                        if (searchTerm.trim().equals(type))
                        {
                            return new String[]{"fileType", FileTypeEnum.translateTypeDEtoE(type)};
                        }
                    }
                    wrongFileTypeSearchTermError();
                }
                case "File-Grösse" -> {
                    if (regexFileSize(searchTerm))
                    {
                        double d = numberCheck(searchTerm);
                        if (!(d < 0))
                        {
                            return new String[]{"fileSize", String.valueOf(d)};
                        }
                    } else {
                        wrongFileSizeInputError();
                    }
                }
                case "Grösseneinheit" -> {
                    if (regexFileSizeUnit(searchTerm))
                    {
                        String unit = searchTerm.toUpperCase();
                        return new String[]{"fileSizeUnit", unit};
                    } else {
                        wrongFileSizeUnitError();
                    }
                }
                case "File-Erstellungsdatum" -> {
                    LocalDate localDate;
                    if (regexTestDate(searchTerm))
                    {
                        localDate = fromStringToDateAndCheck(searchTerm);
                        if (!localDate.equals(LocalDate.of(1, 1, 1)))
                        {
                            return new String[]{"fileDate", localDate.toString()};
                        }
                    } else {
                        wrongDatePickerInputError();
                    }
                }
                case "File-Autor(en)" -> {
                    if (stringCheck(searchTerm))
                    {
                        return new String[]{"fileAuthor", searchTerm};
                    }
                }
                case "File-Besetzung" -> {
                    if (stringCheck(searchTerm))
                    {
                        return new String[]{"fileCast", searchTerm};
                    }
                }
                case "Metadaten-Beschreibung" -> {
                    if (!regexDelimiter(searchTerm))
                    {
                        return new String[]{"description", searchTerm};
                    } else {
                        wrongDelimiterError();
                    }
                }
                default -> failedSearchingError();
            }
        } else {
            noSearchAttributeError();
        }
        return null;
    }

    /**
     * Die Such-Methode leitet die Suchbegriffe und der Speicherort weiter an den Context.
     * @param searchStringArray       Such-Attribut, Suchbegriff und der Ort, der durchsucht werden soll.
     * @param strategyVariable           Die Variable, die anzeigt, wo gespeichert werden soll.
     */
    public void searchMetadata(String[]searchStringArray , int strategyVariable)
    {
        searchAttribute = searchStringArray[0];
        searchTerm = searchStringArray[1];
        oldSaveLocation = searchStringArray[2];
        List<String> list = context.searchStrategy(searchStringArray, strategyVariable);
        // Übergibt die Suchresultaten-Liste weiter, dass sie aufgeteilt werden kann in einzelne Objekte
        fromSearchResultListToObservableList(list);
    }

    /**
     * Methode zum Aufteilen der Suchresultaten-liste. Gibt den String der einzelnen Suchresultate weiter an die
     * "fromSearchResultToMetadata()"-Methode, damit Metadaten-Objekte daraus gemacht werden können.
     * Nachdem alle Einträge der searchResultList in die resultMetadataList gespeichert wurden,
     * wird, mit der "createObservableList()"-Methode, anschliessend eine ObservableList daraus gemacht.
     * @param searchResultList Die Suchresultaten-Liste von der Methode "searchMetadata()".
     */
    public void fromSearchResultListToObservableList(List<String> searchResultList)
    {
        resultObservableList = FXCollections.observableArrayList();

        for (String s : searchResultList)
        {
            resultObservableList.add(fromSearchResultToMetadata(s));
        }
    }

    /**
     * Methode, die aus einem String ein Metadaten-Objekt macht und es in einer ArrayList abspeichert.
     * @param searchResultSet Einen String, der ein Metadaten-Objekt beinhaltet.
     */
    public Metadata fromSearchResultToMetadata(String searchResultSet)
    {
        // Split nach "," und ":"
        List<String[]> itemList = splitItemList(searchResultSet);

        // Hilf-Listen
        List<String> helpItemList1 = new ArrayList<>();
        List<String[]> helpItemList2 = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++)
        {
            helpItemList1.add((itemList.get(i))[1]);
            helpItemList2.add(helpItemList1.get(i).split("[\"]"));
        }

        String fileTypeValue = helpItemList2.get(4)[1];
        String[] fileSizeValue = itemList.get(5);
        String fileSizeString = fileSizeValue[1];
        String[] fileSizeUnitValue = helpItemList2.get(6);

        LocalDate date;
        if (dataSetName.equals(helpItemList2.get(11)[1]))
        {
            String[] fileDateValue = itemList.get(7);
            String[] fileDateGetDate = fileDateValue[2].split("[T]");
            String[] dateComponents = fileDateGetDate[0].split("[-]");
            String[] yearComp = dateComponents[0].split("[\"]");
            int year = Integer.parseInt(yearComp[1]);
            int month = Integer.parseInt(dateComponents[1]);
            int day = Integer.parseInt(dateComponents[2]);
            date = LocalDate.of(year,month,day);
        } else {
            String[] stringList = fileSizeValue[1].split("[\"]");
            fileSizeString = stringList[1];

            String[] dataString1 = itemList.get(7)[1].split("[\"]") ;
            String[] dateString = dataString1[1].split("[-]");
            int year = Integer.parseInt(dateString[0]);
            int month = Integer.parseInt(dateString[1]);
            int day = Integer.parseInt(dateString[2]);
            date = LocalDate.of(year, month, day);
        }

        Metadata m1 = new Metadata(helpItemList2.get(2)[1], helpItemList2.get(3)[1]);
        m1.setMetadataID(helpItemList2.get(1)[1]);
        m1.setFileTypeSearch(fileTypeValue);
        m1.setFileSize(Double.parseDouble(fileSizeString));
        m1.setFileSizeUnitSearch(fileSizeUnitValue[1]);
        m1.setFileDate(date);

        m1.setFileAuthor(helpItemList2.get(8)[helpItemList2.get(8).length-1]);
        m1.setFileCast(helpItemList2.get(9)[helpItemList2.get(9).length-1]);
        m1.setDescription(helpItemList2.get(10)[1]);
        m1.setSaveLocation(helpItemList2.get(11)[1]);

        return m1;
    }

    /**
     * Methode zum Durchführen zweier split()-Operationen. Einmal nach "," und dann nach ":"
     * Die Methode gibt eine ArrayList von String-Arrays zurück.
     * @param itemList  Ein String, der nach "," und ":" Gesplittet werden muss.
     * @return          Eine ArrayList von String-Arrays, die durch das Splitten entstanden ist.
     */
    protected List<String[]> splitItemList(String itemList)
    {
        List<String> itemListStep1 = Arrays.stream(itemList.split("[,]")).toList();
        List<String[]> itemListStep2 = new ArrayList<>();
        itemListStep1.forEach(il1 -> itemListStep2.add(il1.split("[:]")));
        return itemListStep2;
    }

    /**
     * Methode zur Umwandlung von einem Datum zu einem String.
     * @param date  Ein Datum
     * @return      Das Datum als String.
     */
    public String dateToString(LocalDate date)
    {
        String dateString = "";
        if (date.getDayOfMonth() < 10)
        {
            dateString += "0" + date.getDayOfMonth() + ".";
        } else {
            dateString += date.getDayOfMonth() + ".";
        }
        if (date.getMonthValue() < 10)
        {
            dateString += "0" + date.getMonthValue() + ".";
        } else {
            dateString += date.getMonthValue() + ".";
        }
        dateString += date.getYear();
        return dateString;
    }

    /**
     * Methode, um aus einem Metadaten-Objekt ein Item für den ListViewer auf der Such-Resultaten-Seite zu machen.
     * @param metadata  Ein Metadaten-Objekt.
     * @return          Ein String, der in der ListView angezeigt wird.
     */
    public String metadataToStringForResultView(Metadata metadata)
    {
        return "[Metadaten-ID]:\t" + metadata.getMetadataID() + ",\t\t" +
                "[File-ID]:\t" + metadata.getFileID() + ",\t\t" +
                "[File-Titel]:\t"+ metadata.getFileTitle() + ",\t\t" +
                "[File-Typ]:\t" + FileTypeEnum.translateTypeEtoDE(metadata.getFileType()) + ",\n" +
                "[File-Grösse]:\t\t" + metadata.getFileSize() + "  '" + metadata.getFileSizeUnit() + "',\t\t\t" +
                "[Erstellungsdatum]:\t\t" + dateToString(metadata.getFileDate()) + ",\n" +
                "[File-Autor(en)]:\t\"" + metadata.getFileAuthor() + "\",\n" +
                "[File-Besetzung]:\t\"" + metadata.getFileCast() + "\",\n" +
                "[Beschreibung]:\t\"" + metadata.getDescription() + "\",\n" +
                "[Speicherort]:\t\t" + metadata.getSaveLocation() + " ";
    }

    /**
     * Methode, um aus einem Eintrag vom ListViewer in ein Metadaten-Objekt zu machen.
     * @param item  Ein Item vom ListViewer
     * @return      Ein Metadaten-Objekt
     */
    public Metadata itemToMetadata(String item)
    {
        List<String[]> itemList = splitItemList(item);

        String metadataID = itemList.get(0)[1].trim();
        String fileID = itemList.get(1)[1].trim();
        String fileTitle = itemList.get(2)[1].trim();
        String fileTypeString = itemList.get(3)[1].trim();
        String fileType = FileTypeEnum.translateTypeDEtoE(fileTypeString);

        String fileSizeString = itemList.get(4)[1].trim();
        String[] fileSizeList = fileSizeString.split("[']");
        fileSizeString = fileSizeList[0];
        String fileSizeUnit = fileSizeList[1];

        String fileDateString = itemList.get(5)[1].trim();
        String[] fileDateList = fileDateString.split("[.]");
        int day = Integer.parseInt(fileDateList[0]);
        int month = Integer.parseInt(fileDateList[1]);
        int year = Integer.parseInt(fileDateList[2]);

        String[] fileAutorList = itemList.get(6)[1].split("[\"]");
        String[] fileCastList = itemList.get(7)[1].split("[\"]");
        String[] descriptionList = itemList.get(8)[1].split("[\"]");
        String saveLocation = itemList.get(9)[1].trim();

        Metadata m2 = new Metadata(fileID, fileTitle);
        m2.setMetadataID(metadataID);
        m2.setFileTypeSearch(fileType);
        m2.setFileSize(Double.parseDouble(fileSizeString));
        m2.setFileSizeUnitSearch(fileSizeUnit);
        m2.setFileDate(LocalDate.of(year, month, day));
        m2.setFileAuthor(fileAutorList[1]);
        m2.setFileCast(fileCastList[1]);
        m2.setDescription(descriptionList[1]);
        m2.setSaveLocation(saveLocation);

        return m2;
    }

    /**
     * Hilfsmethode für die "dbRadioButton", die auf jeder View zu finden sind. Mal als Suchort, mal als Speicherort.
     * Wenn der dbRadioButton ausgewählt wird, dann wird der folderRadioButton auf "nicht-ausgewählt" gesetzt.
     * @param dbRadioButton        RadioButton für Speichern oder Durchsuchen von der DB.
     * @param dbDetailsLabel       InfoLabel mit dem Inhalt "Datenset-Name", wird sichtbar, wenn DB ausgewählt ist.
     * @param locationDetailLabel  InfoLabel, auf dem der Name des Datensets zu sehen ist, wenn DB ausgewählt ist.
     * @param folderRadioButton    RadioButton für Speichern oder Durchsuchen von des lokalen Ordners.
     * @param folderDetailsLabel   InfoLabel mit dem Inhalt "Ordner auf dem USB-Stick",
     *                             wird unsichtbar, wenn DB ausgewählt ist.
     */
    public void dbRadioButtonHandler(RadioButton dbRadioButton, Label dbDetailsLabel, Label locationDetailLabel,
                                     RadioButton folderRadioButton, Label folderDetailsLabel)
    {
        if (dbRadioButton.isSelected())
        {
            dbDetailsLabel.setVisible(true);
            locationDetailLabel.setText(Model.getModelObj().getDataSetName());
            folderRadioButton.setSelected(false);
            folderDetailsLabel.setVisible(false);
            // Strategy-Variable wird gesetzt, damit die Strategy gewählt werden kann.
            Model.getModelObj().setStrategyVariable(1);
        } else {
            dbRadioButton.setSelected(true);
        }
    }

    /**
     * Hilfsmethode für die "folderRadioButton", die auf jeder View zu finden sind. Als Suchort oder als Speicherort.
     * Wenn der folderRadioButton ausgewählt wird, dann wird der dbRadioButton auf "nicht-ausgewählt" gesetzt.
     * @param folderRadioButton   RadioButton für Speichern oder Durchsuchen von des lokalen Ordners.
     * @param folderDetailsLabel  InfoLabel mit dem Inhalt "Ordner auf dem USB-Stick", wird sichtbar, wenn Folder
     *                            ausgewählt ist.
     * @param locationDetailLabel InfoLabel, auf dem der Name des Ordners zu sehen ist, wenn Folder ausgewählt ist.
     * @param dbRadioButton       RadioButton für Speichern oder Durchsuchen von der DB.
     * @param dbDetailsLabel      InfoLabel mit dem Inhalt "Datenset-Name", wird unsichtbar, wenn Folder ausgewählt ist.
     */
    public void folderRadioButtonHandler(RadioButton folderRadioButton, Label folderDetailsLabel,
                                         Label locationDetailLabel, RadioButton dbRadioButton, Label dbDetailsLabel)
    {
        if (folderRadioButton.isSelected())
        {
            folderDetailsLabel.setVisible(true);
            locationDetailLabel.setText(Model.getModelObj().getFolderFilePath());
            dbRadioButton.setSelected(false);
            dbDetailsLabel.setVisible(false);
            // Strategy-Variable wird gesetzt, damit die Strategy gewählt werden kann.
            Model.getModelObj().setStrategyVariable(2);
        } else {
            folderRadioButton.setSelected(true);
        }
    }

    /**
     * Diese Methode öffnet die StartPage von anderen Seiten/Views.
     * @param event        Mausklick auf einen Button.
     * @throws IOException wird geworfen, wenn der FXML-Loader das fxml-File nicht findet.
     */
    @FXML
    public void openStartWindow(ActionEvent event) throws IOException
    {
        Parent home = FXMLLoader.load(Objects.requireNonNull(Start.class.
                getResource("gui_view/startPageView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(home);
        stage.setScene(scene);
        stage.setTitle("Willkommen auf der Startseite");
        stage.show();
    }

    /**
     * Diese Methode öffnet die Such-Resultat-Seite von den anderen Seiten/Views.
     * @param event        Mausklick auf einen Button.
     * @throws IOException wird geworfen, wenn der FXML-Loader das fxml-File nicht findet.
     */
    @FXML
    public void openSearchResultWindow(ActionEvent event) throws IOException
    {
        Parent searching = FXMLLoader.load(Objects.requireNonNull(Start.class.
                getResource("gui_view/searchResultView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(searching);
        stage.setScene(scene);
        stage.setTitle("Suchfunktion");
        stage.show();
    }

    /**
     * Öffnet die Seite mit der Eingabemaske, für die Erstellung eines neuen Datensatzes
     * @param event        ein Mausklick startet die Methode
     * @throws IOException wird geworfen, wenn der FXML-Loader das fxml-File nicht findet.
     */
    @FXML
    public void openDataInputMaskWindow(ActionEvent event) throws IOException
    {
        Parent searching = FXMLLoader.load(Objects.requireNonNull(
                Start.class.getResource("gui_view/dataInputMaskView.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(searching);
        stage.setScene(scene);
        if (createNewMetadata)
        {
            stage.setTitle("Neue Metadaten hinzufügen");
        } else {
            stage.setTitle("Metadaten bearbeiten");
        }
        stage.show();
    }

    // Getter
    /**
     * Getter für das Singleton-Objekt
     * @return Das Singleton-Objekt
     */
    public static Model getModelObj() { return MODEL; }

    public String getDataSetName() { return dataSetName; }

    public String getFolderFilePath() { return folderFilePath; }

    public boolean getCreateNewMetadata() { return createNewMetadata; }

    public int getStrategyVariable() { return strategyVariable; }

    public String getSearchTerm() { return searchTerm; }

    public String getSearchAttribute() { return searchAttribute; }

    public String getOldSaveLocation() { return oldSaveLocation; }

    public ObservableList<Metadata> getResultObservableList() { return resultObservableList; }

    public Metadata getTemporaryMetadata() { return temporaryMetadata; }

    public String getValueAttributeChoiceBox() { return valueAttributeChoiceBox; }

    // Setter
    public void setCreateNewMetadata(boolean createNewMetadata) { this.createNewMetadata = createNewMetadata; }

    public void setStrategyVariable(int strategyVariable) { this.strategyVariable = strategyVariable; }

    public void setSearchResultItem(Metadata metadata) { temporaryMetadata = metadata; }

    public void setValueAttributeChoiceBox(String value) { valueAttributeChoiceBox = value; }
}

