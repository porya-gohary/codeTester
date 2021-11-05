package com.tester.codetester;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class mainController {
    @FXML
    private MenuButton menuButton;


    @FXML
    void openAction(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
//            openFile(file);
        }
    }

    @FXML
    void exit(ActionEvent actionEvent) {
        Platform.exit();
    }


}