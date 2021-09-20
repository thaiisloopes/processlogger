package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Execution {
    String time;
    String agent;
    String place;
    String product;
    String factor;

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
}
