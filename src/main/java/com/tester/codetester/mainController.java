package com.tester.codetester;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
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
import org.apache.commons.io.FilenameUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class mainController {
    List<testCase> testCaseList = new ArrayList<>();
    String language="";
    int pass = 0;
    ObservableList<PieChart.Data> pieChartData;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    private ListView<testCase> testCasesList = new ListView<>();;
    PseudoClass inactive = PseudoClass.getPseudoClass("inactive");
    PseudoClass active = PseudoClass.getPseudoClass("active");


    @FXML
    private TextField codeAddr;

    @FXML
    private TextArea terminalText;

    @FXML
    private PieChart pieChart;

    @FXML
    private MenuButton selectLanguage;


    @FXML
    void selectCode() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            codeAddr.setText(file.getAbsolutePath());
        }
    }


    @FXML
    void importTestCases() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            if(!FilenameUtils.getExtension(file.getAbsolutePath()).equals("xml")){
                showErrorMessage("The selected test-cases file is incorrect!");
            }else {
//            openFile(file);
                readXML(file);
                clearItems();
                updateTestCasesListView();
                showPieChart();
            }
        }
    }

    @FXML
    void openTestCaseDetails(javafx.scene.input.MouseEvent event) {
        if (event.getSource() == testCasesList && event.getClickCount() == 2 && (testCasesList.getSelectionModel().getSelectedItem() != null)) {
            System.out.println(testCasesList.getSelectionModel().getSelectedIndex());
            showTestCaseWindow(testCasesList.getSelectionModel().getSelectedIndex());

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
        stage.setTitle("Test Case #" + (index+1) + " Details");
        stage.setScene(scene);

        root.setEffect(new DropShadow());
        stage.show();
        detailsCotroller detailsCotroller = loader.getController();
        detailsCotroller.setDetails(testCaseList.get(index).getInput(), testCaseList.get(index).getOutput());


    }

    @FXML
    void updateTestCasesListView(){
        testCasesList.getItems().clear();
        for (int i = 1; i <= testCaseList.size(); i++) {
            int finalI = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    testCasesList.getItems().add(testCaseList.get(finalI -1));
                }
            });

        }

        testCasesList.setCellFactory(lv -> new ListCell<testCase>() {
            @Override
            protected void updateItem(testCase item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    pseudoClassStateChanged(inactive, false);
                }else{
                    if(item.isPass()) {
                        setText(item.getName() + "\t[PASS]");
                        pseudoClassStateChanged(active, true);
                    }
                    else {
                        setText(item.getName() + "\t[FAIL]");
                        pseudoClassStateChanged(inactive, true);
                    }
                }

            }
        });


    }

    @FXML
    void startTesting() {
        if (Objects.equals(codeAddr.getText(), "")) {
            showErrorMessage("Please select your code.");
        } else {
            if (language.equals("Select") || language.equals("")) {
                showErrorMessage("Please select your programming language.");
            } else {
                if (language.equals("Python3")) {
                    pass = 0;
                    runPythonCode();
                    terminalText.setWrapText(true);
                } else if (language.equals("C++")) {

                }
            }
        }
    }

    @FXML
    void languageSelector(ActionEvent event) {
        MenuItem mi = (MenuItem) event.getSource();
        language = mi.getText();
        selectLanguage.setText(mi.getText());
    }


    void showPieChart() {
        pieChart.setLegendVisible(true);
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Failed", testCaseList.size() - pass),
                new PieChart.Data("Passed", pass));
        pieChart.setData(pieChartData);
    }

    void updatePieChart() {
        for (PieChart.Data d : pieChartData) {
            if (d.getName().equals("Failed")) {
                d.setPieValue(testCaseList.size() - pass);
            } else {
                d.setPieValue(pass);

            }
        }
    }

    @FXML
    void chartClickHandler(javafx.scene.input.MouseEvent event) {
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

    @FXML
    void clearTerminal(){
        terminalText.clear();
    }

    void runPythonCode() {

        pythonThread pythonThread = new pythonThread(this, testCaseList, "python3", codeAddr.getText());
        pythonThread.start();
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);

        alert.setContentText(message);
        alert.showAndWait();
    }

    void readXML(File file) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            if (document.hasChildNodes()) {
                NodeList caseList = document.getElementsByTagName("case");
                for (int count = 0; count < caseList.getLength(); count++) {
                    Node elemNode = caseList.item(count);
                    if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) elemNode;
//                        read input
                        NodeList inputList = eElement.getElementsByTagName("input");
                        Node inputNode = inputList.item(0);
//                        read output
                        NodeList outputList = eElement.getElementsByTagName("output");
                        Node outputNode = outputList.item(0);
                        testCaseList.add(new testCase(count, inputNode.getTextContent(), outputNode.getTextContent()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void printResult() {
        double passPercent = ((double) pass / testCaseList.size()) * 100;
        double failPercent = 100 - passPercent;
        terminalText.appendText("Final Result:\n");
        terminalText.appendText("Passed:\t" + passPercent + "%\n");
        terminalText.appendText("Failed:\t" + failPercent + "%\n");
        terminalText.appendText("============================================" + "\n");
    }

    void clearItems() {
        testCasesList.getItems().clear();
    }

    void appendToTerminal(String text) {
        terminalText.appendText(text);
    }

    void setPass(int pass) {
        this.pass = pass;
    }

    void updateProgressBar(double progress){
        progressBar.setProgress(progress);
    }

    void setStatusLabel(String status){
        statusLabel.setText(status);
    }
}