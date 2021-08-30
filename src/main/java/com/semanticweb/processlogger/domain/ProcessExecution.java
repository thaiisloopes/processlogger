package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProcessExecution {
    String time;
    String agent;
    String place;
    String product;
    String factor;
    String relatedTo;
    List<ProcessExecution> subProcesses;

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

    public String getRelatedTo() {
        return relatedTo;
    }

    public List<ProcessExecution> getSubProcesses() {
        return subProcesses;
    }
}
