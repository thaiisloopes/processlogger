package com.semanticweb.processlogger.applications.resources;

import com.semanticweb.processlogger.applications.execution.resources.ActivityExecutionStatus;

public class Execution {
    private String time;
    private String agent;
    private String place;
    private String product;
    private String factor;
    private String start;
    private String end;
    private ActivityExecutionStatus status;

    public String getTime() {
        return time;
    }

    public String getAgent() {
        return agent;
    }

    public String getPlace() {
        return place;
    }

    public String getProduct() {
        return product;
    }

    public String getFactor() {
        return factor;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public ActivityExecutionStatus getStatus() {
        return status;
    }
}
