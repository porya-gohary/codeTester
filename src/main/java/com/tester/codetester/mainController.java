package com.tester.codetester;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class mainController {
    List<testCase> testCaseList = new ArrayList<>();
    String language;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ListView testCasesList;

    @FXML
    private TextField codeAddr;

    @FXML
    private  TextArea terminalText;

    @FXML
    private PieChart pieChart;

    @FXML
    private MenuButton selectLanguage;


    @FXML
    void selectCode(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            codeAddr.setText(file.getAbsolutePath());
        }
    }



    @FXML
    void importTestCases(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
//            openFile(file);
            readXML(file);
            clearItems();
            addTestCases();
        }
        updatePieChart();
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
        stage.setResizable(false);


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
    void startTesting(){
        if(Objects.equals(codeAddr.getText(), "")){
            showErrorMessage("Please select your code.");
        }else{
            if (language.equals("Select") || language.equals("")){
                showErrorMessage("Please select your programming language.");
            }else{
                if(language.equals("Python3")){
                    runPythonCode();
                }else if(language.equals("C++")){

                }
            }
        }
    }

    @FXML
    void languageSelector(ActionEvent event){
        MenuItem mi = (MenuItem) event.getSource();
        language=mi.getText();
        selectLanguage.setText(mi.getText());
    }


    @FXML
    void updatePieChart(){
        pieChart.setLegendVisible(true);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Failed", 13),
                new PieChart.Data("Passed",    25));
        pieChart.setData(pieChartData);
    }

    @FXML
    void chartClickHandler(javafx.scene.input.MouseEvent event){
        final Label caption = new Label("");
        caption.setTextFill(Color.WHITE);
        caption.setStyle("-fx-font: 12 arial;");
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());

                    caption.setText(String.valueOf(data.getPieValue()));
                }
            });
        }
    }

    @FXML
    void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    void runPythonCode(){
        ProcessBuilder processBuilder = new ProcessBuilder("python3", codeAddr.getText());
        processBuilder.redirectErrorStream(true);
        InputStream stdout = null;
        try {
            Process process = processBuilder.start();
            stdout=process.getInputStream();
            int exitCode = process.waitFor();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                terminalText.appendText(line+"\n");
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }

    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);

        alert.setContentText(message);
        alert.showAndWait();
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