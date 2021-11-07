package com.tester.codetester;

public class testCase {
    private String input,output;
    private String name;
    private int id;
    private boolean pass;
    public testCase(int id, String input, String output){
        this.id=id;
        this.input=input;
        this.output=output;
        pass = false;
        name = "Test Case #"+(id+1);
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

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Test Case #"+getId()+ ":\n" +"Input:\n"+ getInput()+"\n" +"Output:\n"+ getOutput()+"\n";
    }
}
