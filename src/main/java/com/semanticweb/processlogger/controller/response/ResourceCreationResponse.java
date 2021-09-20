package com.semanticweb.processlogger.controller.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResourceCreationResponse {

    String uri;

    public ResourceCreationResponse(String uri) {
        this.uri = uri;
    }
}