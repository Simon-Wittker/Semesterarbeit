module project.simtv_mam_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;


    opens project.simtv_mam_app to javafx.fxml;
    opens  project.simtv_mam_app.gui_view to javafx.fxml;
    opens project.simtv_mam_app.gui_model to javafx.fxml;
    opens project.simtv_mam_app.gui_controller to javafx.fxml;
    exports project.simtv_mam_app;
    exports project.simtv_mam_app.gui_controller;
    exports project.simtv_mam_app.gui_model;
}