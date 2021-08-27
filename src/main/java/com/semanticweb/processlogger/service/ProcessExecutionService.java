package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.repository.ProcessExecutionRepository;
import com.stardog.stark.BNode;
import com.stardog.stark.Values;
import org.apache.jena.rdf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class ProcessExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionService.class);

    @Autowired
    private ProcessExecutionRepository processExecutionRepository;

    public Model getProcess() {
        logger.info("Calling repository to get all recorded process execution");

        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = processExecutionRepository.get(queryString);
        triples.stream().forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public List<ResourceCreationResponse> record(List<ProcessExecution> processes) {
        logger.info("Calling repository to save a list of process execution");

        List<List<Triple>> processExecutionToEvent = processes.stream()
                .map(this::buildProcessTriples)
                .collect(Collectors.toList());

        processExecutionRepository.save(processExecutionToEvent);

        return buildResourceCreationResponse(processExecutionToEvent);
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

    public void deleteProcessExecutionFrom(List<ProcessExecution> processes, String graph) {
        logger.info("Calling repository to delete a list of process execution");

        List<List<Triple>> processExecutionToEvent = processes.stream()
                .map(this::buildProcessTriples)
                .collect(Collectors.toList());

        processExecutionRepository.deleteManyTriples(processExecutionToEvent, graph);
    }

    public void deleteGraph(String graph) {
        logger.info("Calling repository to delete a specific graph");

        processExecutionRepository.deleteGraph(graph);
    }

    private List<Triple> buildProcessTriples(ProcessExecution processExecution) {
        String resourceUri = "http://www.example.com/process-execution/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, "http://www.w3.org/2000/01/rdf-schema#Class", "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#agent", processExecution.getResponsible()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#product", processExecution.getProduct()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#factor", processExecution.getFactor()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", processExecution.getTime()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#place", processExecution.getPlace())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
