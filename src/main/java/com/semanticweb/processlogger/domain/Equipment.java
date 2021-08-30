package com.semanticweb.processlogger.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Equipment {

    String code;
    String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
