package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Task {

    String name;
    String description;
    String type;
    String process;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return "https://www.irit.fr/recherches/MELODI/ontologies/BBO#" + type;
    }

    public String getProcess() {
        return process;
    }
}
