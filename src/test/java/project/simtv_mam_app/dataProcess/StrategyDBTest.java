package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.Metadata;
import project.simtv_mam_app.gui_model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyDBTest
{
    Metadata m1;
    StrategyDB strategyDB;
    Model model;

    @BeforeEach
    void testSetup()
    {
        strategyDB = new StrategyDB("DataSet1");
        m1 = new Metadata("TestID42", "TestDaten");
        m1.createID();
        m1.setSaveLocation("DataSet1");
        model = new Model();
    }

    @Test
    void dataSaveTest()
    {
        strategyDB.save(m1);
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), m1.getSaveLocation()},1);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyDB.delete(m1.getMetadataID(), m1.getSaveLocation());
    }

    @Test
    void searchDataTest()
    {
        m1.createID();
        strategyDB.save(m1);
        List<String> list = strategyDB.search(new String[]{"metadataID", m1.getMetadataID(), m1.getSaveLocation()});
        model.fromSearchResultListToObservableList(list);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyDB.delete(m1.getMetadataID(), m1.getSaveLocation());
        List<String> list1 = new ArrayList<>(); // Leere Liste.
        assertEquals(list1, strategyDB.search(new String[]{"metaDataID", m1.getMetadataID(), m1.getSaveLocation()}));
        assertEquals(list1, strategyDB.search(new String[]{"", m1.getMetadataID(), m1.getSaveLocation()}));
    }

    @Test
    void updateDataTest()
    {
        m1.createID();
        strategyDB.save(m1);
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), m1.getSaveLocation()}, 1);
        m1 = model.getResultObservableList().get(0);
        m1.setDescription("Nö");
        strategyDB.update(m1,m1.getMetadataID(), m1.getSaveLocation());
        model.searchMetadata(new String[]{"metadataID", m1.getMetadataID(), m1.getSaveLocation()}, 1);
        assertEquals(m1.toString(), model.getResultObservableList().get(0).toString());
        strategyDB.delete(m1.getMetadataID(), m1.getSaveLocation());
    }

    @Test
    void deleteDataTest()
    {
        assertTrue(strategyDB.delete(m1.getMetadataID(), m1.getSaveLocation()));
        assertFalse(strategyDB.delete(m1.getMetadataID(), "Dataset1"));
    }
}
