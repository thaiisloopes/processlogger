package com.semanticweb.processlogger.controllers.resources;

public class ResourceCreationResponse {

    private final String uri;

    public ResourceCreationResponse(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}