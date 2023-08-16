package com.semanticweb.processlogger.applications.execution.resources;

import java.util.List;

public class Flow {
    private FlowStatus flowStatus;
    private String from;
    private String to;
    private List<String> steps;

    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<String> getSteps() {
        return steps;
    }
}
