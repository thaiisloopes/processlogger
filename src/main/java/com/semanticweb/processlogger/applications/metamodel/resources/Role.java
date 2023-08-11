package com.semanticweb.processlogger.applications.metamodel.resources;

import java.util.List;

public class Role {
    private String name;

    private List<String> relatedActivities;

    public String getName() {
        return name;
    }

    public List<String> getRelatedActivities() {
        return relatedActivities;
    }
}
