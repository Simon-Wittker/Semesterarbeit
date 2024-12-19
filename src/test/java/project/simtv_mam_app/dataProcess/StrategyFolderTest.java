package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.Metadata;
import project.simtv_mam_app.gui_model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StrategyFolderTest
{
    Metadata m1;
    StrategyFolder strategyFolder;
    Model model;
    Random random;
    int randInt;
    String folderFilePath;

    @BeforeEach
    void testSetup()
    {
        strategyFolder = new StrategyFolder();
        random = new Random();
        randInt = random.nextInt(500);
        model = new Model();
        folderFilePath = model.getFolderFilePath();
        m1 = new Metadata("TestID" + randInt, "TestDaten" + randInt);
        m1.createID();
        m1.setSaveLocation(folderFilePath + "/SaveFile_"
                + m1.getMetadataID() + "_.txt");
    }

    @Test
    void dataSaveTest() {
        strategyFolder.save(m1);
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), folderFilePath}, 2);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyFolder.delete(m1.getMetadataID(), m1.getSaveLocation());
        Metadata m2 = new Metadata("id111", "T111");
        m1.createID();
        // m2 ist ohne saveLocation!
        try {
            assertThrows(ExceptionInInitializerError.class, () -> strategyFolder.save(m2));
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Test
    void searchDataTest()
    {
        strategyFolder.save(m1);
        List<String> list = strategyFolder.search(new String[]{"metadataID", m1.getMetadataID(), folderFilePath});
        model.fromSearchResultListToObservableList(list);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyFolder.delete(m1.getMetadataID(), m1.getSaveLocation());
        List<String> list2 = strategyFolder.search(new String[]{"metadataID", m1.getMetadataID(), folderFilePath});
        model.fromSearchResultListToObservableList(list2);
        assertNotEquals(m1.toString(), model.getResultObservableList().toString());
        assertThrows(IndexOutOfBoundsException.class, () -> model.getResultObservableList().get(0));
    }

    @Test
    void updateDataTest()
    {
        strategyFolder.save(m1);
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), folderFilePath}, 2);
        m1 = model.getResultObservableList().get(0);
        m1.setDescription("Nö");
        strategyFolder.update(m1, m1.getMetadataID(), folderFilePath);
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), folderFilePath}, 2);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyFolder.delete(m1.getMetadataID(), m1.getSaveLocation());
    }

    @Test
    void deleteDataTest() {
        strategyFolder.save(m1);
        assertTrue(strategyFolder.delete(m1.getMetadataID(), m1.getSaveLocation()));
        assertFalse(strategyFolder.delete(m1.getMetadataID() + "1", m1.getSaveLocation()));
    }
}
