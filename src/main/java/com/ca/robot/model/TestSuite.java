package com.ca.robot.model;

import java.util.List;

public class TestSuite extends TestData {
    //private Object metadata; // [];
    private int numberOfTests; // 552;
    private String source; // "C:\\code\\rp-plugins\\integrationTests\\src\\main\\cdd-plugins";
    private String relativeSource; // "file:///C:/code/rp-plugins/integrationTests/src/main/cdd-plugins";
    private List<TestSuite> suites; // [;
    private List<TestCase> tests; // [;

    public int getNumberOfTests() {
        return numberOfTests;
    }

    public void setNumberOfTests(int numberOfTests) {
        this.numberOfTests = numberOfTests;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRelativeSource() {
        return relativeSource;
    }

    public void setRelativeSource(String relativeSource) {
        this.relativeSource = relativeSource;
    }

    public List<TestSuite> getSuites() {
        return suites;
    }

    public void setSuites(List<TestSuite> suites) {
        this.suites = suites;
    }

    public List<TestCase> getTests() {
        return tests;
    }

    public void setTests(List<TestCase> tests) {
        this.tests = tests;
    }
}