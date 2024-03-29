package com.semanticweb.processlogger.applications.execution;

import com.semanticweb.processlogger.applications.execution.resources.SubProcessExecution;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import com.semanticweb.processlogger.repositories.resources.Triple;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.f4b6a3.ulid.UlidCreator.getUlid;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SubProcessExecutionApplication {
    private static final Logger logger = getLogger(SubProcessExecutionApplication.class);
    private static final String RDF_TYPE_CLASS_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String TIME_INTERVAL_CLASS_URI = "http://www.w3.org/2006/time#Interval";
    private static final String TIME_INSTANT_CLASS_URI = "http://www.w3.org/2006/time#Instant";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";
    private static final String TIME_AT_PROPERTY_URI = "http://www.w3.org/2006/time#at";
    private static final String TIME_START_PROPERTY_URI = "http://www.w3.org/2006/time#start";
    private static final String TIME_END_PROPERTY_URI = "http://www.w3.org/2006/time#end";
    private static final String EVENT_TIME_PROPERTY_URI = "http://purl.org/NET/c4dm/event.owl#time";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(
            SubProcessExecution subProcessExecution,
            String processId,
            String processExecutionId,
            String subProcessId
    ) throws URISyntaxException {
        logger.info("Calling repository to save a subprocess execution");

        List<Triple> subProcessExecutionToTriples = buildTriples(subProcessExecution, processId, processExecutionId, subProcessId);

        repository.save(subProcessExecutionToTriples);

        return buildResourceCreationResponse(subProcessExecutionToTriples);
    }

    private List<Triple> buildTriples(
            SubProcessExecution subProcessExecution,
            String processId,
            String processExecutionId,
            String subProcessId
    ) {
        String resourceUri = "http://www.example.com/conference/processes/" + processId
                + "/executions/" + processExecutionId
                + "/subprocesses/" + subProcessId + "/executions/" + getUlid();

        List<Triple> triples = new ArrayList<>();

        triples.addAll(asList(
                buildTriple(resourceUri, RDF_TYPE_CLASS_URI, "http://purl.org/saeg/ontologies/bpeo#SubProcessExecution"),
                buildTriple(resourceUri, RDF_TYPE_CLASS_URI, subProcessExecution.getType()),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, subProcessExecution.getName()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#executor", subProcessExecution.getExecutor()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#activityStatus", subProcessExecution.getStatus())
        ));

        triples.addAll(
                subProcessExecution.getInputArguments().stream().map(argument ->
                        buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#inputArgument", argument)
                ).collect(toList())
        );

        triples.addAll(
                subProcessExecution.getOutputArguments().stream().map(argument ->
                        buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#outputArgument", argument)
                ).collect(toList())
        );

        List<Triple> timeTriples = buildTimeTriples(subProcessExecution.getStart(), subProcessExecution.getEnd(), resourceUri);
        triples.addAll(timeTriples);

        return triples;
    }

    private List<Triple> buildTimeTriples(LocalDateTime start, LocalDateTime end, String resourceUri) {
        String randomTimeUri = "http://www.example.com/conference/times/" + getUlid();
        String timeClass = TIME_INTERVAL_CLASS_URI;

        if(end != null && start.isEqual(end)) timeClass = TIME_INSTANT_CLASS_URI;

        List<Triple> triples = new ArrayList<>();

        if(timeClass.equals(TIME_INSTANT_CLASS_URI)) {
            triples.add(
                    buildTriple(randomTimeUri, TIME_AT_PROPERTY_URI, start.toString())
            );
        }
        triples.add(buildTriple(randomTimeUri, TIME_START_PROPERTY_URI, start.toString()));

        if(end != null) {
            triples.add(buildTriple(randomTimeUri, TIME_END_PROPERTY_URI, end.toString()));
        }

        triples.addAll(
                asList(
                        buildTriple(resourceUri, EVENT_TIME_PROPERTY_URI, randomTimeUri),
                        buildTriple(randomTimeUri, RDF_TYPE_CLASS_URI, timeClass)
                )
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
