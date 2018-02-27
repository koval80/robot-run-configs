package com.ca.robot.model;

public class TestModel {
    private String generated;
    private String generatedMillis;
    private String title;
    private TestSuite suite;

    public TestSuite getSuite() {
        return suite;
    }

    public void setSuite(TestSuite suite) {
        this.suite = suite;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public String getGeneratedMillis() {
        return generatedMillis;
    }

    public void setGeneratedMillis(String generatedMillis) {
        this.generatedMillis = generatedMillis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}