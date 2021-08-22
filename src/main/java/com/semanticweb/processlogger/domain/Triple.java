package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Triple {
    String resource;
    String property;
    String value;

    public Triple(String resource, String property, String value) {
        this.resource = resource;
        this.property = property;
        this.value = value;
    }

    public String getResource() {
        return resource;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}
