package project.simtv_mam_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Dies ist die Start-Klasse, mit der die Applikation gestartet wird.
 * @author Simon Wittker
 */
public class Start extends Application
{

    /**
     * Startmethode für das GUI. Eröffnet mit der Startseite.
     * @param stage        Das Fenster, bzw die Bühne, auf der die Applikation stattfindet.
     * @throws IOException wird geworfen, wenn der FXML-Loader das fxml-File nicht findet.
     */
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("gui_view/startPageView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Willkommen beim SimTV Media-Asset-Management-System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Hauptmethode
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args)
    {
        launch();
    }
}