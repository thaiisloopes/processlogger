package com.semanticweb.processlogger.applications.execution.resources;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class Flow {
    private FlowStatus flowStatus;
    @Nullable
    private String from;
    @Nullable
    private String to;
    @Nullable
    private List<String> steps;
    private String belongsTo;
    private Boolean initialFlow;
    public FlowStatus getFlowStatus() {
        return flowStatus;
    }
    @Nullable
    public String getFrom() {
        return from;
    }
    @Nullable
    public String getTo() {
        return to;
    }

    @Nullable
    public List<String> getSteps() {
        return steps;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public Boolean getInitialFlow() {
        return initialFlow;
    }
}
