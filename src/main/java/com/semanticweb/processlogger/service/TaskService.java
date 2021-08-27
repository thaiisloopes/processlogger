package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Task;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.repository.TaskRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String TASK_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Task";
    private static final String THING_CLASS_URI = "https://schema.org/Thing";
    private static final String SCHEMA_NAME_PROPERTY_URI = "https://schema.org/name";
    private static final String SCHEMA_DESCRIPTION_PROPERTY_URI = "https://schema.org/description";
    private static final String SCHEMA_IS_PART_OF_PROPERTY_URI = "https://schema.org/isPartOf";

    @Autowired
    private TaskRepository taskRepository;

    public Model getTasks() {
        logger.info("Calling repository to get all recorded tasks");

        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = taskRepository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public List<ResourceCreationResponse> save(List<Task> tasks) {
        logger.info("Calling repository to save a list of tasks");

        List<List<Triple>> tasksToTriples = tasks.stream()
                .map(this::buildTaskTriples)
                .collect(toList());

        taskRepository.save(tasksToTriples);

        return buildResourceCreationResponse(tasksToTriples);
    }

    private List<ResourceCreationResponse> buildResourceCreationResponse(List<List<Triple>> triples) {
        List<ResourceCreationResponse> resources = new ArrayList<>();

        triples.forEach(
                tripleList -> tripleList.stream()
                        .findFirst()
                        .ifPresent(
                                triple -> resources.add(
                                        new ResourceCreationResponse(triple.getResource(), "")
                                )
                        )
        );

        return resources;
    }

    private List<Triple> buildTaskTriples(Task task) {
        String resourceUri = "http://www.example.com/task/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_URI + "type", TASK_CLASS_URI),
                buildTriple(resourceUri, RDF_URI + "type", THING_CLASS_URI),
                buildTriple(resourceUri, SCHEMA_NAME_PROPERTY_URI, task.getName()),
                buildTriple(resourceUri, SCHEMA_DESCRIPTION_PROPERTY_URI, task.getDescription()),
                buildTriple(resourceUri, SCHEMA_IS_PART_OF_PROPERTY_URI, task.getProcess())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
