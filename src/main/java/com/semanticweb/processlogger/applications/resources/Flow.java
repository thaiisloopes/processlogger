package com.semanticweb.processlogger.applications.resources;

public class Flow {
    private FlowStatus flowStatus;

    private Integer currentNode;

    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

    public Integer getCurrentNode() {
        return currentNode;
    }
}