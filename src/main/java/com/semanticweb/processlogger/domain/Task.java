package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Task {

    String name;
    String description;
    String process;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProcess() {
        return process;
    }
}