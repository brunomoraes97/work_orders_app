package com.xptoinfo.workorders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class WorkOrderApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorkOrderApp.class.getResource("workOrderGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 540);
        stage.setTitle("XPTO Informática");
        Image icon = new Image(Objects.requireNonNull(WorkOrderApp.class.getResourceAsStream("app_icon.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}