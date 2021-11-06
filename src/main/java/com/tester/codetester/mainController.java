package com.tester.codetester;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mainController {
    List<testCase> testCaseList = new ArrayList<>();

    @FXML
    private MenuButton menuButton;

    @FXML
    private ListView testCasesList;






    @FXML
    void openAction(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
//            openFile(file);
            readXML(file);
            clearItems();
            addTestCases();
        }
    }

    @FXML
    void addTestCases(){
        for (int i=1;i<=testCaseList.size();i++){
            int finalI = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    testCasesList.getItems().add("Test Case #"+ finalI);
                }
            });
        }
    }

    @FXML
    void openTestCaseDetails(javafx.scene.input.MouseEvent event) {
        if (event.getSource() == testCasesList && event.getClickCount() == 2 && (testCasesList.getSelectionModel().getSelectedItem() != null)){
            showTestCaseWindow(Integer.parseInt(testCasesList.getSelectionModel().getSelectedItem().toString().substring(11)));

        }
    }

    @FXML
    void showTestCaseWindow(int index) {

        //            root = FXMLLoader.load(getClass().getResource("test-case-details.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("test-case-details.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = new Stage();


//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Test Case #"+index+" Details");
        stage.setScene(scene);

        root.setEffect(new DropShadow());
        stage.show();
        index--;
        detailsCotroller detailsCotroller = loader.getController();
        detailsCotroller.setDetails(testCaseList.get(index).getInput(),testCaseList.get(index).getOutput());

    }



    @FXML
    void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    void readXML(File file){
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            if (document.hasChildNodes())
            {
                NodeList caseList = document.getElementsByTagName("case");
                for (int count = 0; count < caseList.getLength(); count++) {
                    Node elemNode = caseList.item(count);
                    if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) elemNode;
//                        read input
                        NodeList inputList=eElement.getElementsByTagName("input");
                        Node inputNode = inputList.item(0);
//                        read output
                        NodeList outputList=eElement.getElementsByTagName("output");
                        Node outputNode = outputList.item(0);
                        testCaseList.add(new testCase(count,inputNode.getTextContent(),outputNode.getTextContent()));
                    }
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    void clearItems() {
        testCasesList.getItems().clear();
    }
}