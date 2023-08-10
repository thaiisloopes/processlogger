package com.semanticweb.processlogger.applications.metamodel.resources;

import java.util.List;

public class Process {
    private String description;
    private List<String> hasPart;

    public String getDescription() {
        return description;
    }

    public List<String> getHasPart() {
        return hasPart;
    }
}
