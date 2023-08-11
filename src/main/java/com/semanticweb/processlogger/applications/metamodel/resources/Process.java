package com.semanticweb.processlogger.applications.metamodel.resources;

import java.util.List;

public class Process {
    private String name;
    private List<String> flowElements;

    public String getName() {
        return name;
    }

    public List<String> getFlowElements() {
        return flowElements;
    }
}
