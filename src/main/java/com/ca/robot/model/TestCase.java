package com.ca.robot.model;

import java.util.List;

public class TestCase extends TestData {
    private List<String> tags;
    private String timeout;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
