package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProcessExecution {
    String time;
    String responsible;
    String place;
    String product;
    String factor;

    public String getTime() {
        return time;
    }

    public String getResponsible() {
        return responsible;
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
