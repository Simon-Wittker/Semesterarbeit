package project.simtv_mam_app;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Diese Klasse beinhaltet alle Meldungen für die Anwendung.
 * Sei es Bestätigungen, Informationsmeldungen oder Fehlermeldungen.
 * @author Simon Wittker
 */
public class Messages {
    /**
     * Bestätigungsfenster, wenn die Datenmasken-Seite ohne Speichern verlassen wird.
     * @return Das Bestätigungsfenster.
     */
    public static Optional<ButtonType> confirmationMaskExit()
    {
        return new Alert(Alert.AlertType.CONFIRMATION,
                "Wenn die Seite verlassen, werden die Daten nicht gespeichert. Seite trotzdem verlassen?")
                .showAndWait();
    }

    /**
     * Bestätigungsfenster, für das Löschen.
     * @return Das Bestätigungsfenster
     */
    public static Optional<ButtonType> confirmationDelete()
    {
        return new Alert(Alert.AlertType.CONFIRMATION,
                "Wollen Sie die Metadaten wirklich unwiderruflich löschen?")
                .showAndWait();
    }

    /**
     * Bestätigungsfenster, dass der Speicherort geändert werden soll und das alte File dann gelöscht wird.
     * @return Das Bestätigungsfenster.
     */
    public static Optional<ButtonType> confirmationSaveLocationChance()
    {
        return new Alert(Alert.AlertType.CONFIRMATION,
                "Wenn Sie den Speicherort ändern, wird das bestehende Metadaten-File gelöscht." +
                        " Wollen Sie den Speicherort also ändern?").showAndWait();
    }

    /**
     * Informationsmeldung, dass die Daten erfolgreich gespeichert wurden.
     */
    public static void successfulSaveInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Die Daten wurden gespeichert.").showAndWait();
    }

    /**
     * Informationsmeldung, dass die Daten nicht gespeichert wurden.
     */
    public static void failedSaveInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Die Daten konnten nicht gespeichert.").showAndWait();
    }

    /**
     * Informationsmeldung, dass das Metadaten-File gelöscht wurde.
     */
    public static void successfulDeletedInfo()
    {
        new Alert(Alert.AlertType.INFORMATION, "Das Metadaten-File wurde erfolgreich gelöscht.")
                .showAndWait();
    }

    /**
     * Informationsmeldung, dass das Metadaten-File nicht gelöscht werden konnte.
     */
    public static void failedDeletedInfo()
    {
        new Alert(Alert.AlertType.INFORMATION, "Ein Fehler ist aufgetreten. Das Metadaten-File wurde nicht " +
                "gelöscht, bitte versuchen Sie es später noch einmal.").showAndWait();
    }

    /**
     * Informationsmeldung, wenn in das Such-Resultaten-Feld geklickt wurde und es keine Such-Resultate anzeigt.
     */
    public static void noItemInListViewerInfo()
    {
        new Alert(Alert.AlertType.INFORMATION, "Keine Suchresultate geladen.").showAndWait();
    }

    /**
     * Informationsmeldung, dass das Metadaten-Files nicht geladen konnten.
     */
    public static void failedLoadDataError()
    {
        new Alert(Alert.AlertType.ERROR, "Die Metadaten-Files konnten nicht von lokalen Ordner" +
                " geladen werden. Bitte überprüfen Sie, ob der USB-Stick richtig verbunden ist.").showAndWait();
    }

    /**
     * Fehlermeldung, wenn der FXMLLoader die View-Datei nicht laden konnte.
     */
    public static void failedViewLoadError()
    {
        new Alert(Alert.AlertType.ERROR, "Schwerer Fehler ist aufgetreten! Die aufgerufene Seite konnte" +
                " nicht geladen werden. Bitte starten Sie die Anwendung neu.").showAndWait();
    }

    /**
     * Fehlermeldung, wenn keine Verbindung zur MongoDB hergestellt werden konnte.
     */
    public static void failedDBConnectionError()
    {
        new Alert(Alert.AlertType.ERROR, "Es konnte keine Verbindung zur MongoDB hergestellt werden. " +
                "Bitte überprüfen Sie Ihre Internet-Verbindung und versuchen Sie es später noch einmal.").showAndWait();
    }

    /**
     * Fehlermeldung, wenn eines der Pflichtfelder (File-ID oder File-Titel) leer ist
     * und auf "Daten speichern" geklickt wurde.
     */
    public static void missingDataInputError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte die ** Pflichtfelder ** ausfüllen!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in das TextField von File-Grösse eine ungültige Eingabe gemacht wurde.
     */
    public static void wrongFileSizeInputError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte nur positive Zahlen als File-Grösse eingeben und " +
                "statt eines Kommas einen Punkt verwenden! Bsp. 1.5 ").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in das TextField von File-Grösse eine negative Zahl eingegeben wurde.
     */
    public static void negativNumberError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte eine positive Zahl eingeben!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in die Felder "File-Autor" oder "File-Cast" eine ungültige Eingabe
     * (z.B. Zahlen) gemacht wurde.
     */
    public static void wrongStringInputError()
    {
        new Alert(Alert.AlertType.ERROR,
                "Bitte für die File-Autoren und die File-Besetzung nur Buchstaben verwenden!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in eines der Felder "File-Autor", "File-Cast" oder "Beschreibung"
     * als Trennzeichen ein "," oder ":" verwendet wurde.
     */
    public static void wrongDelimiterError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte kein \",\" oder \":\" als Trennzeichen verwenden! " +
                        "Stattdessen ein \";\" oder \".\" verwenden." +
                "Und Anführungs- und Schlusszeichen -> \" \"  sind nicht zulässig!!!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in das Datumsfeld kein gültiges Datum geschrieben wurde
     */
    public static void wrongDatePickerInputError()
    {
        new Alert(Alert.AlertType.ERROR,
                "Bitte ein Datum eingeben als Tag.Monat.Jahr! Format: dd.mm.yyyy ").showAndWait();
    }

    /**
     * Fehlermeldung, wenn in das Erstellungsdatum-TextField ein ungültiges Datum eingegeben wurde.
     */
    public static void wrongDateExceptionError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte ein gültiges Datum eingeben!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn kein Speicherort ausgewählt und auf "Daten speichern" geklickt wurde.
     */
    public static void missingSaveLocationError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte wählen sie einen Speicherort aus.").showAndWait();
    }

    /**
     * Fehlermeldung, wenn keinen Suchbegriff eingegeben wurde und auf "Suchen" geklickt wurde
     */
    public static void noSearchTermError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte einen Suchbegriff eingeben!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn kein Such-Attribut eingegeben und auf "Suchen" geklickt wurde.
     */
    public static void noSearchAttributeError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte Such-Attribut auswählen!").showAndWait();
    }

    /**
     * Fehlermeldung, wenn zu dem Such-Attribut "File-Typ" in das Suchfeld
     * eine ungültige Typenbezeichnung eingegeben wurde.
     */
    public static void wrongFileTypeSearchTermError()
    {
        new Alert(Alert.AlertType.ERROR,
                "Die Auswahl für File-Typ ist: Unbekannt, Text, Text mit Bild(er), Bild, Audio (Podcast)," +
                        " Audio (Musik), Video (Serien), Video (Filme), Video (Sendungen).").showAndWait();
    }

    /**
     * Fehlermeldung, wenn zu dem Such-Attribut "Grösseneinheit" in das Suchfeld
     * eine ungültige Grössenbezeichnung eingegeben wurde.
     */
    public static void wrongFileSizeUnitError()
    {
        new Alert(Alert.AlertType.ERROR,
                "Die Grösseneinheit kann entweder \"B\" für bytes, \"KB\" für KiloBytes, \"MB\" " +
                        "für MegaBytes oder \"GB\" für Gigabytes heissen) z.B (1.5 B und nicht 1,5B ) ").showAndWait();
    }

    /**
     * Fehlermeldung, wenn kein Ort zum Durchsuchen angewählt wurde und auf "Suchen" geklickt wurde.
     */
    public static void missingSearchLocationChoiceError()
    {
        new Alert(Alert.AlertType.ERROR, "Bitte den Ort zum Durchsuchen auswählen").showAndWait();
    }

    public static void failedSearchingError()
    {
        new Alert(Alert.AlertType.ERROR, "Es ist ein Fehler aufgetreten. " +
                "Die Suche konnte nicht durchgeführt werden. Bitte starten Sie die Anwendung neu.").showAndWait();
    }

    public static void wrongStrategyChoseError()
    {
        new Alert(Alert.AlertType.ERROR, "Es wurde eine ungültige Strategie ausgewählt. " +
                "Bitte gültige Strategie wählen!").showAndWait();
    }
}
