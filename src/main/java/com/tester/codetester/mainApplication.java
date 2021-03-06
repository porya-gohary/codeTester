package com.tester.codetester;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainApplication extends Application {
    @Override
    public void start(Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("main-view.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 485, 620);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scene.getStylesheets().add(String.valueOf(mainApplication.class.getResource("pie-chart-custom-colors.css")));
                stage.setTitle("Code Tester");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}