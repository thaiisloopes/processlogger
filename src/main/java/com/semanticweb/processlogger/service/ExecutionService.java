package com.semanticweb.processlogger.service;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.domain.Execution;
import com.semanticweb.processlogger.repository.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;

import static com.github.f4b6a3.ulid.UlidCreator.getUlid;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ExecutionService {

    private static final Logger logger = getLogger(ExecutionService.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String SCHEMA_IS_PART_OF_PROPERTY_URI = "https://schema.org/isPartOf";

    @Autowired
    private TripleRepository repository;

    public Model getProcess() {
        logger.info("Calling repository to get all recorded process execution");

        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = repository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public ResourceCreationResponse recordProcessExecution(Execution execution, String processId) {
        logger.info("Calling repository to save a process execution");

        List<Triple> processExecutionToEvent = buildProcessTriples(execution, processId);

        repository.save(processExecutionToEvent);

        return buildResourceCreationResponse(processExecutionToEvent);
    }

    public ResourceCreationResponse recordTaskExecution(
            Execution execution,
            String processId,
            String processExecutionId,
            String taskId
    ) {
        logger.info("Calling repository to save a task execution");

        List<Triple> taskExecutionToEvent = buildTaskTriples(execution, processId, processExecutionId, taskId);

        repository.save(taskExecutionToEvent);

        return buildResourceCreationResponse(taskExecutionToEvent);
    }

    public ResourceCreationResponse recordSubProcessExecution(
            Execution execution,
            String processId,
            String processExecutionId,
            String subProcessId
    ) {
        logger.info("Calling repository to save a subProcess execution");

        List<Triple> subProcessExecutionToEvent = buildSubProcessTriples(
                execution, processId, processExecutionId, subProcessId);

        repository.save(subProcessExecutionToEvent);

        return buildResourceCreationResponse(subProcessExecutionToEvent);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    public void deleteProcessExecutionFrom(List<Execution> processes, String graph) {
        logger.info("Calling repository to delete a list of process execution");

        List<List<Triple>> processExecutionToEvent = processes.stream()
                .map((Execution execution) -> buildProcessTriples(execution, ""))
                .collect(toList());

        repository.deleteManyTriples(processExecutionToEvent, graph);
    }

    public void deleteGraph(String graph) {
        logger.info("Calling repository to delete a specific graph");

        repository.deleteGraph(graph);
    }

    private List<Triple> buildProcessTriples(Execution execution, String processId) {
        String resourceUri = "http://www.example.com/processes/" + processId + "/executions/" + getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(resourceUri, RDF_TYPE_URI, "http://www.example.com/processes/" + processId),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#agent", execution.getAgent()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#product", execution.getProduct()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#factor", execution.getFactor()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", execution.getTime()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#place", execution.getPlace()),
                buildTriple(resourceUri, SCHEMA_IS_PART_OF_PROPERTY_URI, execution.getRelatedTo())
        );
    }

    private List<Triple> buildTaskTriples(
            Execution execution,
            String processId,
            String processExecutionId,
            String taskId
    ) {
        String resourceUri = "http://www.example.com/processes/" + processId
                + "/executions/" + processExecutionId
                + " /tasks/" + taskId + "/executions/" + getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(resourceUri, RDF_TYPE_URI, "http://www.example.com/tasks/" + taskId),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#agent", execution.getAgent()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#product", execution.getProduct()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#factor", execution.getFactor()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", execution.getTime()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#place", execution.getPlace()),
                buildTriple("http://www.example.com/processes/" + processId + "/executions/" + processExecutionId,
                        "http://purl.org/NET/c4dm/event.owl#sub_event",
                        resourceUri),
                buildTriple(resourceUri, SCHEMA_IS_PART_OF_PROPERTY_URI, execution.getRelatedTo())
        );
    }

    private List<Triple> buildSubProcessTriples(
            Execution execution,
            String processId,
            String processExecutionId,
            String subProcessId
    ) {
        String resourceUri = "http://www.example.com/processes/" + processId
                + "/executions/" + processExecutionId
                + " /processes/" + subProcessId + "/executions/" + getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(resourceUri, RDF_TYPE_URI, "http://www.example.com/processes/" + subProcessId),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#agent", execution.getAgent()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#product", execution.getProduct()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#factor", execution.getFactor()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", execution.getTime()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#place", execution.getPlace()),
                buildTriple("http://www.example.com/processes/" + processId + "/executions/" + processExecutionId,
                        "http://purl.org/NET/c4dm/event.owl#sub_event",
                        resourceUri),
                buildTriple(resourceUri, SCHEMA_IS_PART_OF_PROPERTY_URI, execution.getRelatedTo())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
