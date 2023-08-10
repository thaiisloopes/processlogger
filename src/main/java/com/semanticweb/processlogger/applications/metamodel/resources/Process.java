package com.semanticweb.processlogger.applications.metamodel.resources;

import java.util.List;

public class Process {
    //TODO: validar os outros ToDos
    //TODO: Criar Controller e Application do InputOutputSpecification e do SubProcessos
    //TODO: Validar que Controller e Application de Process e Task estão fazendo o desejado
    private String name;
    private String description;
    private List<String> hasPart;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHasPart() {
        return hasPart;
    }
}
