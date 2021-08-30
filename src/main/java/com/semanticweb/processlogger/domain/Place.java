package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Place {

    String description;

    public String getDescription() {
        return description;
    }
}
