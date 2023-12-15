package com.semanticweb.processlogger.applications.execution.resources;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SubProcessExecution {
    private String name;
    private String type;
    private String executor;
    private String status;
    private List<String> inputArguments;
    private List<String> outputArguments;
    private LocalDateTime start;
    @Nullable
    private LocalDateTime end;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getExecutor() {
        return executor;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getInputArguments() {
        return inputArguments;
    }

    public List<String> getOutputArguments() {
        return outputArguments;
    }

    public LocalDateTime getStart() {
        return start;
    }

    @Nullable
    public LocalDateTime getEnd() {
        return end;
    }
}
