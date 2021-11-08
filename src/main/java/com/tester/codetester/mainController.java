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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.*;
import org.apache.commons.io.FilenameUtils;
import javafx.application.HostServices;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class mainController {
    List<testCase> testCaseList = new ArrayList<>();
    String language="";
    int pass = 0;
    ObservableList<PieChart.Data> pieChartData;
    private HostServices hostServices ;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ImageView readyImage;

    @FXML
    private Label statusLabel;

    @FXML
    private ListView<testCase> testCasesList = new ListView<>();
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
    public void initialize() {
        showProgressIndicator(false);
        showPieChart();
    }

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
                updateTestCasesListView();
                updatePieChart();
            }
        }
    }

    @FXML
    void openTestCaseDetails(javafx.scene.input.MouseEvent event) {
        if (event.getSource() == testCasesList && event.getClickCount() == 2 && (testCasesList.getSelectionModel().getSelectedItem() != null)) {
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
        stage.initStyle(StageStyle.UTILITY);
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
                        pseudoClassStateChanged(inactive, false);
                        pseudoClassStateChanged(active, true);
                    }
                    else {
                        setText(item.getName() + "\t[FAIL]");
                        pseudoClassStateChanged(active, false);
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
            } else  if(testCaseList.size()==0){
                showErrorMessage("Please select your test cases file.");
            }else {
                if (language.equals("Python3")) {
                    pass = 0;
                    runPythonCode();
                    terminalText.setWrapText(true);
                } else if (language.equals("C++")) {
                    pass = 0;
                    runCppCode();
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
//        set dummy numbers for just show the chart
        pieChart.setLegendVisible(true);
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Failed", 10 - pass),
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

    @FXML
    void saveTerminal(){
//        open file chooser for save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File file =fileChooser.showSaveDialog(new Stage());

//        write terminal to selected file
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(terminalText.getText());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void viewAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        FlowPane fp = new FlowPane();
        Label lbl = new Label("Developed By: Pourya Gohari \nMore information: ");
        Hyperlink link = new Hyperlink("https://github.com/porya-gohary/codeTester");
        fp.getChildren().addAll( lbl, link);

        link.setOnAction( (evt) -> {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://www.github.com/porya-gohary/codeTester"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } );

        alert.getDialogPane().contentProperty().set( fp );

//        alert.setContentText("Developed By: Pourya Gohari \n More information: "+link);
        alert.showAndWait();
    }


    void runPythonCode() {

        pythonThread pythonThread = new pythonThread(this, testCaseList, "python3", codeAddr.getText());
        pythonThread.start();
    }

    void runCppCode() {
        cppThread cppThread = new cppThread(this, testCaseList, codeAddr.getText());
        cppThread.start();
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);

        alert.setContentText(message);
        alert.showAndWait();
    }

    void readXML(File file) {
        int index = testCaseList.size();
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
                        testCaseList.add(new testCase(index, inputNode.getTextContent(), outputNode.getTextContent()));
                        index++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void printResult(String elapsedTime) {
        double passPercent = ((double) pass / testCaseList.size()) * 100;
        double failPercent = 100 - passPercent;
        terminalText.appendText("Final Result:\n");
        terminalText.appendText("Passed:\t" + String.format("%.2f", passPercent) + "%\n");
        terminalText.appendText("Failed:\t" + String.format("%.2f", failPercent) + "%\n");
        terminalText.appendText("Elapsed time:\t" + elapsedTime + "\n");
        terminalText.appendText("============================================" + "\n");
    }

    @FXML
    void clearTestCases() {
        testCasesList.getItems().clear();
        testCaseList.clear();
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

    void showProgressIndicator(boolean show){
        progressIndicator.setVisible(show);
        readyImage.setVisible(!show);
    }
}