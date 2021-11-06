package com.tester.codetester;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class detailsCotroller {

    @FXML
    private TextArea detailsInput,detailsOutput;
    @FXML
    private Button closeDetail;


    @FXML
    void close(ActionEvent actionEvent) {
        if (actionEvent.getSource() == closeDetail) {
            Stage stage = (Stage) closeDetail.getScene().getWindow();
            stage.close();
        }
    }

    void setDetails(String input, String output){
        detailsInput.setText(input);
        detailsOutput.setText(output);
    }
}
