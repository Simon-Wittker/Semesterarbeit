package project.simtv_mam_app.dataProcess;

import project.simtv_mam_app.dataRessource.Metadata;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static project.simtv_mam_app.Messages.failedLoadDataError;
import static project.simtv_mam_app.Messages.failedSaveInfo;


/**
 * Eine Strategy-Implementierung, die als Ziel ein lokaler Ordner hat.
 * @author Simon Wittker
 */
public class StrategyFolder implements Strategy
{

    Charset charset = StandardCharsets.UTF_16;

    private List<String> bigDataList;

    /**
     * Hilfsmethode, der ein Such-Attribut in einen Index umwandelt.
     * Wird bei der "search()"-Methode verwendet,
     * damit die Methode weiss, an welcher Stelle die Werte verglichen werden müssen.
     * @param searchAttribute               Der String mit dem Such-Attribut.
     * @return                              Ein Index, der auf die Stelle mit dem gewünschten Attribut zeigt.
     */
    private int getIndexOfAttribute(String searchAttribute)
    {
        switch(searchAttribute)
        {
            case "metadataID" -> { return 1; }
            case "fileID" -> { return 2; }
            case "fileTitle" -> { return 3; }
            case "fileType" -> { return 4; }
            case "fileSize" -> { return 5; }
            case "fileSizeUnit" -> { return 6; }
            case "fileDate" -> { return 7; }
            case "fileAuthor" -> { return 8; }
            case "fileCast" -> { return 9; }
            case "description" -> { return 10; }
            case "saveLocation" -> { return 11; }
        }
        return 0;
    }


    /**
     * Hilfsmethode, die alle Files im lokalen Ordner liest und in eine Liste packt, die dann durchsucht werden kann.
     * @return Eine Liste mit je einem String pro File im Zielordner.
     */
    private List<String> readAllFilesInFolderAndSaveAsList(String searchLocation)
    {
        bigDataList = new ArrayList<>();
        File f = new File(searchLocation);
        File[] files = f.listFiles();

        String line;

        if (files != null)
        {
            for (int i = 0; i < Objects.requireNonNull(files).length; i++)
            {

                try (FileReader reader = new FileReader(files[i].getAbsolutePath(), charset);
                     BufferedReader bufferedReader = new BufferedReader((reader))){
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        bigDataList.add(line);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    failedLoadDataError();
                }
            }
        } else {
            failedLoadDataError();
        }
        return bigDataList;
    }


    /**
     * Metadaten in ein File speichern.
     * @param metadata Metadaten-Objekt zum Speichern.
     */
    @Override
    public void save(Metadata metadata)
    {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(metadata.getSaveLocation(), charset )))
        {
           out.write((metadata.toStringForSaveInFolder()));
         } catch (Exception e)
        {
            e.printStackTrace();
            failedSaveInfo();
        }
    }

    /**
     * Metadaten-File im lokalen Ordner suchen.
     * @param searchStringArray       Such-Attribut, Suchbegriff und der Ort, der durchsucht werden soll.
     * @return                        Eine Liste mit einem String, der die Daten der Treffer beinhaltet.
     */
    @Override
    public List<String> search(String[] searchStringArray)
    {
        List<String> resultList = new ArrayList<>();
        String[] helpStringList1;
        String[] helpStringList2;

        try
        {
            bigDataList = readAllFilesInFolderAndSaveAsList(searchStringArray[2]);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        int index = getIndexOfAttribute(searchStringArray[0]);

        for (String s : bigDataList)
        {
            if (s.trim().contains("{"))
            {
                helpStringList1 = s.split("[,]");
                helpStringList2 = helpStringList1[index].split("[\"]");
                try {
                    if (helpStringList2[3].contains(searchStringArray[1])) {
                        resultList.add(s);
                    }
                } catch (ArrayIndexOutOfBoundsException e )
                {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    /**
     * Bestehendes Metadaten-File updaten, indem das Alte gelöscht wird und das Geänderte gespeichert wird.
     * @param metadata          Metadaten-Objekt zum Speichern.
     * @param oldMetadataID     Die Id des alten Metadaten-File, das ersetzt wird.
     * @param oldSaveLocation      Der Ort, an dem das zu-Updatende File gespeichert ist.
     */
    @Override
    public void update(Metadata metadata, String oldMetadataID, String oldSaveLocation)
    {
        if (delete(oldMetadataID, oldSaveLocation))
        {
            save(metadata);
        }
    }

    /**
     * Ausgewähltes Metadaten-File aus dem lokalen Ordner löschen.
     * @param metadataID    Id des Metadaten-Files, das gelöscht werden soll.
     * @param saveLocation  Der Ort, an dem das zu-Löschende File gespeichert ist.
     * @return              → true, wenn die Löschung geklappt hat.
     *                      → false, wenn die Löschung nicht geklappt hat.
     */
    @Override
    public boolean delete(String metadataID, String saveLocation)
    {
        String[] saveLocationPeaces = saveLocation.split("[/]");
        String savePath = "/" + saveLocationPeaces[1] + "/" + saveLocationPeaces[2] + "/" +  saveLocationPeaces[3];
        File f = new File(savePath);
        File[] files = f.listFiles();
        for(int i = 0; i < Objects.requireNonNull(files).length; i++)
        {
            String path = files[i].getAbsolutePath();
            if (path.equals(savePath + "/SaveFile_" + metadataID + "_.txt"))
            {
                if(files[i].delete())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
