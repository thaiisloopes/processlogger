package com.semanticweb.processlogger.applications.resources;

import java.util.List;

public class Process {

    String name;
    String description;
    List<String> hasPart;

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
