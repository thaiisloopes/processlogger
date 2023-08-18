package com.semanticweb.processlogger.applications.execution;

import com.semanticweb.processlogger.applications.execution.resources.ProcessExecution;
import com.semanticweb.processlogger.applications.execution.resources.SubProcessExecution;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import com.semanticweb.processlogger.repositories.resources.Triple;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static com.github.f4b6a3.ulid.UlidCreator.getUlid;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ProcessExecutionApplication {
    private static final Logger logger = getLogger(ProcessExecutionApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(
            ProcessExecution processExecution,
            String processId
    ) throws URISyntaxException {
        logger.info("Calling repository to save a process execution");

        List<Triple> processExecutionToTriples = buildTriples(processExecution, processId);

        repository.save(processExecutionToTriples);

        return buildResourceCreationResponse(processExecutionToTriples);
    }

    // TODO: verificar se esta certo como adicionar o time
    private List<Triple> buildTriples(
            ProcessExecution processExecution,
            String processId
    ) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/processes/" + processId + "/executions/" + getUlid();

        List<Triple> triples = asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/saeg/ontologies/bpeo#ProcessExecution"),
                buildTriple(resourceUri, RDF_TYPE_URI, processExecution.getType()),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, processExecution.getName()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#executor", processExecution.getExecutor()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#activityStatus", processExecution.getStatus()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", processExecution.getStart().toString()),
                buildTriple(resourceUri, "http://purl.org/NET/c4dm/event.owl#time", processExecution.getEnd().toString())
        );

        triples.addAll(
                processExecution.getInputArguments().stream().map(argument ->
                        buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#inputArgument", argument)
                ).collect(toList())
        );

        triples.addAll(
                processExecution.getOutputArguments().stream().map(argument ->
                        buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#outputArgument", argument)
                ).collect(toList())
        );

        triples.addAll(
                processExecution.getFlows().stream().map(flow ->
                        buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#flow", flow)
                ).collect(toList())
        );

        return triples;
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
