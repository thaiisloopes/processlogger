package com.semanticweb.processlogger.applications.execution.resources;

import java.util.List;
import java.util.Optional;

public class Flow {
    private FlowStatus flowStatus;
    private Optional<String> from;
    private Optional<String> to;
    private List<String> steps;
    private String belongsTo;

    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

    public Optional<String> getFrom() {
        return from;
    }

    public Optional<String> getTo() {
        return to;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getBelongsTo() {
        return belongsTo;
    }
}
