package project.simtv_mam_app.gui_model;

import project.simtv_mam_app.Start;
import project.simtv_mam_app.dataRessource.Metadata;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ModelTest
{
    Metadata m1;
    private Model model;
    Random random;
    int randInt;

    @BeforeEach
    void setup()
    {
        model = new Model();
        random = new Random();
        randInt = random.nextInt(500);
    }

    @Test
    void createATemporaryMetadataTest()
    {
        String metadataID = "";
        String fileID = "id1";
        String fileID2 = "id2";
        String fileTitle = "title1";
        String fileType = "Text";
        double fileSize = 1.1;
        int fileSizeUnit = 2;
        LocalDate date = LocalDate.of(2021,12,1);
        String fileAuthor = "Lorem";
        String fileCast = "ipsum und co.";
        String description = "Nö";
        String saveLocation = "DataSet1";
        m1 = new Metadata(fileID, fileTitle);
        m1.createID();
        m1.setFileType(fileType);
        m1.setFileSize(fileSize);
        m1.setFileSizeUnit(fileSizeUnit);
        m1.setFileDate(date);
        m1.setFileAuthor(fileAuthor);
        m1.setFileCast(fileCast);
        m1.setDescription(description);
        m1.setSaveLocation(saveLocation);
        model.createATemporaryMetadata(metadataID, fileID, fileTitle, fileType,
                fileSize, fileSizeUnit, date, fileAuthor, fileCast, description, saveLocation);
        // auch wenn alle Einträge gleich sind, so darf es nicht dasselbe Objekt geben!
        // Die Generierung der MetadatenID sorgt dafür.
        assertNotEquals(m1.toString(), model.getTemporaryMetadata().toString());
        m1.setFileID(fileID2);
        m1.createID();
        model.createATemporaryMetadata(m1.getMetadataID(), fileID2, fileTitle, fileType,
                fileSize, fileSizeUnit, date, fileAuthor, fileCast, description, saveLocation);
        // wenn dasselbe MetadatenID benutzt wird, ist es dasselbe!
        assertEquals(m1.toString(), model.getTemporaryMetadata().toString());
    }

    @Test
    void metadataIDCheckTest()
    {
        m1 = new Metadata("id1", "Title1");
        m1.createID();
        String metadataID = m1.getMetadataID();
        String[] metadataPieces = metadataID.split("[_]");
        m1.setFileID("id2");
        String checkedMetadataID = model.metadataIDCheck(m1, metadataID);
        assertNotEquals(metadataID, checkedMetadataID);
        assertEquals(checkedMetadataID, m1.getFileID() + "_" + m1.getFileTitle() + "_" + metadataPieces[2]);
    }

    @Test
    void searchTermAndAttributCheckTest()
    {
        String attribute1 = "Metadaten_ID";
        String attribute2 = "File-ID";
        String attribute3 = "File-Titel";
        String attribute4 = "File-Typ";
        String attribute5 = "File-Grösse";
        String attribute6 = "Grösseneinheit";
        String attribute7 = "File-Erstellungsdatum";
        String attribute8 = "File-Autor(en)";
        String attribute9 = "File-Besetzung";
        String attribute10 = "Metadaten-Beschreibung";
        String searchTerm1 = "EinString";
        String searchTerm2 = "Text";
        String searchTerm3 = "1.1";
        String searchTerm4 = "B";
        String searchTerm5 = "01.01.2001";
        assertEquals(Arrays.toString(new String[]{"metadataID", "EinString"}), Arrays.toString(model.searchTermAndAttributCheck(attribute1, searchTerm1)));
        assertEquals(Arrays.toString(new String[]{"fileID", "EinString"}), Arrays.toString(model.searchTermAndAttributCheck(attribute2, searchTerm1)));
        assertEquals(Arrays.toString(new String[]{"fileTitle", "EinString"}), Arrays.toString(model.searchTermAndAttributCheck(attribute3, searchTerm1)));
        assertEquals(Arrays.toString(new String[]{"fileType", "TEXT"}), Arrays.toString(model.searchTermAndAttributCheck(attribute4, searchTerm2)));
        assertEquals(Arrays.toString(new String[]{"fileSize", "1.1"}), Arrays.toString(model.searchTermAndAttributCheck(attribute5, searchTerm3)));
        assertEquals(Arrays.toString(new String[]{"fileSizeUnit", "B"}), Arrays.toString(model.searchTermAndAttributCheck(attribute6, searchTerm4)));
        assertEquals(Arrays.toString(new String[]{"fileDate", "2001-01-01"}), Arrays.toString(model.searchTermAndAttributCheck(attribute7, searchTerm5)));
        assertEquals(Arrays.toString(new String[]{"fileAuthor", ""}), Arrays.toString(model.searchTermAndAttributCheck(attribute8, "")));
        assertEquals(Arrays.toString(new String[]{"fileCast", "EinString"}), Arrays.toString(model.searchTermAndAttributCheck(attribute9, searchTerm1)));
        assertEquals(Arrays.toString(new String[]{"description", "EinString"}), Arrays.toString(model.searchTermAndAttributCheck(attribute10, searchTerm1)));
    }

    @Test
    void searchMetadataTest()
    {
        m1 = new Metadata("id1111", "T11");
        m1.setMetadataID("id1111_T11_111111");
        m1.setFileDate(LocalDate.of(2011,11,11));
        m1.setFileAuthor("Lorem");
        m1.setFileCast("ipsum und co.");
        m1.setSaveLocation("DataSet1");
        model.setSearchResultItem(m1);
        model.setStrategyVariable(1);
        model.saveMetadata();
        model.searchMetadata(new String[]{"fileID", "id1111", "DataSet1"},1);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());

        model.searchMetadata(new String[]{"fileID", "id1", "DataSet1"},1);
        assertNotEquals(m1.toString(), model.getResultObservableList().get(0).toString());
    }

    @Test
    void splitItemListTest()
    {
        m1 = new Metadata("id77", "T77");
        m1.setMetadataID("id77_T77_260");
        String string = m1.toStringForSaveInFolder();
        List<String> stringList1 = Arrays.stream(string.split("[,]")).toList();
        List<String[]> stringList2 = new ArrayList<>();
        stringList1.forEach(il1 -> stringList2.add(il1.split("[:]")));
        assertEquals(Arrays.toString(stringList2.get(0)), Arrays.toString(model.splitItemList(string).get(0)));
        assertEquals(Arrays.toString(stringList2.get(3)), Arrays.toString(model.splitItemList(string).get(3)));
        assertEquals(Arrays.toString(stringList2.get(7)), Arrays.toString(model.splitItemList(string).get(7)));
        assertNotEquals(Arrays.toString(stringList2.get(0)), Arrays.toString(model.splitItemList(string).get(1)));
        assertNotEquals(Arrays.toString(stringList2.get(5)), Arrays.toString(model.splitItemList(string).get(4)));
    }

    @Test
    void dateToString()
    {
        LocalDate date = LocalDate.of(2021,2,1);
        assertEquals("01.02.2021", model.dateToString(date));
        assertNotEquals("1.2.2021", model.dateToString(date));
        assertNotEquals("01.2.2021", model.dateToString(date));
        assertNotEquals("1.02.2021", model.dateToString(date));
    }

    @Test
    void itemToMetadataTest()
    {
        m1 = new Metadata("id77", "T77");
        m1.setMetadataID("id77_T77_260");
        m1.setFileDate(LocalDate.of(1970,1,2));
        m1.setFileAuthor("Lorem");
        m1.setFileCast("ipsum und co.");
        m1.setSaveLocation("/Volumes/SimTV_MAM1/Lokaler Ordner/SaveFile_id77_T77_260_.txt");
        String item = model.metadataToStringForResultView(m1);
        assertEquals(m1.toString(), model.itemToMetadata(item).toString());
        m1.setFileTitle("t77");
        assertNotEquals(m1.toString(), model.itemToMetadata(item).toString());
    }

    @Test
    void openSearchingWindow()
    {
        Assertions.assertThrows(IOException.class, () -> {
            Parent home =  FXMLLoader.load(Objects.requireNonNull(Start.class.getResource("")));
            new Scene(home);
        });
    }
}
