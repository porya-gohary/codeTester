package com.tester.codetester;

import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class pythonThread extends Thread{
    private String command;
    private String address;
    mainController controller;
    List<testCase> testCaseList = new ArrayList<>();
    int pass =0;

    public pythonThread (mainController controller,List<testCase> testCaseList,String command, String address){
        this.command=command;
        this.address=address;
        this.controller=controller;
        this.testCaseList=testCaseList;

    }

    void createInput(int index){
        try {
            FileWriter fileWriter = new FileWriter("input.txt");
            fileWriter.write(testCaseList.get(index).getInput());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void runPythonCode(int index){
        Platform.runLater(() -> {
        controller.appendToTerminal("Test case #"+index+"\n");
        });
        ProcessBuilder processBuilder = new ProcessBuilder(command, address);
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
        Platform.runLater(() -> {
            controller.appendToTerminal("std output: "+"\n");
        });
        try{
            while((line = reader.readLine()) != null){
                String finalLine = line;
                Platform.runLater(() -> {
                    controller.appendToTerminal(finalLine +"\n");
                });
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }
    }

    void checkOutput(int index){
        String data = "";
        File file = new File("output.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data += scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String finalData = data;
        Platform.runLater(() -> {
        controller.appendToTerminal("-------------------------------------------"+"\n");
        controller.appendToTerminal("Input:"+"\n");
        controller.appendToTerminal(testCaseList.get(index).getInput()+"\n");
        controller.appendToTerminal("Expected output:"+"\n");
        controller.appendToTerminal(testCaseList.get(index).getOutput()+"\n");
        controller.appendToTerminal("Code output:"+"\n");
        controller.appendToTerminal(finalData +"\n");
        });
        if(data.equals(testCaseList.get(index).getOutput())){
            pass++;
            controller.testCaseList.get(index).setPass(true);
            Platform.runLater(() -> {
                controller.appendToTerminal("Test Result: [PASS]"+"\n");
            });
        }else{
            Platform.runLater(() -> {
                controller.appendToTerminal("Test Result: [FAIL]"+"\n");
            });
        }
        Platform.runLater(() -> {
            controller.appendToTerminal("============================================"+"\n");
        });
    }

    public void run() {
        for (int i = 0; i < testCaseList.size(); i++) {
            createInput(i);
            runPythonCode(i);
            checkOutput(i);
            controller.setPass(pass);
        }
//        controller.updatePieChart();
        Platform.runLater(() -> {
            controller.printResult();
            controller.updatePieChart();
            controller.updateTestCasesListView();
        });

    }
}
