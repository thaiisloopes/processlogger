package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.repository.ProcessExecutionRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ProcessExecutionService {

    private static final Logger logger = getLogger(ProcessExecutionService.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String SCHEMA_IS_PART_OF_PROPERTY_URI = "https://schema.org/isPartOf";

    @Autowired
    private ProcessExecutionRepository processExecutionRepository;

    public Model getProcess() {
        logger.info("Calling repository to get all recorded process execution");

        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = processExecutionRepository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public ResourceCreationResponse record(ProcessExecution processExecution, String processId) {
        logger.info("Calling repository to save a process execution");

        List<Triple> processExecutionToEvent = buildProcessTriples(processExecution, processId);

        processExecutionRepository.save(processExecutionToEvent);

        return buildResourceCreationResponse(processExecutionToEvent);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource(), "");
    }

    public void deleteProcessExecutionFrom(List<ProcessExecution> processes, String graph) {
        logger.info("Calling repository to delete a list of process execution");

        List<List<Triple>> processExecutionToEvent = processes.stream()
                .map((ProcessExecution processExecution) -> buildProcessTriples(processExecution, ""))
                .collect(toList());

        processExecutionRepository.deleteManyTriples(processExecutionToEvent, graph);
    }

    public void deleteGraph(String graph) {
        logger.info("Calling repository to delete a specific graph");

        processExecutionRepository.deleteGraph(graph);
    }

    private List<Triple> buildProcessTriples(ProcessExecution processExecution, String processId) {
        String resourceUri = "http://www.example.com/processes/" + processId + "/executions/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(resourceUri, RDF_TYPE_URI, "http://www.example.com/processes/" + processId),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#agent", processExecution.getAgent()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#product", processExecution.getProduct()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#factor", processExecution.getFactor()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", processExecution.getTime()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#place", processExecution.getPlace()),
                buildTriple(resourceUri, SCHEMA_IS_PART_OF_PROPERTY_URI, processExecution.getRelatedTo())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
