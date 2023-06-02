package com.semanticweb.processlogger.applications.resources;

import java.util.List;

public class Process {

    private String name;
    private String description;
    private List<String> hasPart;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHasPart() {
        return hasPart;
    }
}
