package com.semanticweb.processlogger.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResourceCreationResponse {

    String uri;
    String description;

    public ResourceCreationResponse(String uri, String description) {
        this.uri = uri;
        this.description = description;
    }
}