package com.tester.codetester;

public class testCase {
    private String input,output;
    private int id;
    public testCase(int id, String input, String output){
        this.id=id;
        this.input=input;
        this.output=output;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Test Case #"+getId()+ ":\n" +"Input:\n"+ getInput()+"\n" +"Output:\n"+ getOutput()+"\n";
    }
}
