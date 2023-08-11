package com.semanticweb.processlogger.applications.metamodel.resources;

import java.util.List;

public class SubProcess {
    private String name;
    private String inputOutputSpecification;

    private List<String> flowElements;

    public String getName() {
        return name;
    }

    public String getInputOutputSpecification() {
        return inputOutputSpecification;
    }

    public List<String> getFlowElements() {
        return flowElements;
    }
}
