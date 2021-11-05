package com.tester.codetester;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mainController {
    List<testCase> testCaseList = new ArrayList<>();

    @FXML
    private MenuButton menuButton;


    @FXML
    void openAction(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
//            openFile(file);
            readXML(file);
        }
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
}