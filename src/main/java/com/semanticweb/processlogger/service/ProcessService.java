package com.semanticweb.processlogger.service;

import com.semanticweb.processlogger.domain.Process;
import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.repository.ProcessRepository;
import com.stardog.stark.BNode;
import com.stardog.stark.Values;
import org.apache.jena.rdf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    private ProcessRepository processRepository;

    public Model getProcess() {
        logger.info("Calling repository to get all recorded process execution");

        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Process> processes = processRepository.get(queryString);
        processes.stream().forEach(
                process -> {
                    Resource resource = model.createResource(process.getResource());
                    Property property = model.createProperty(process.getProperty());
                    model.add(resource, property, process.getValue());
                }
        );

        return model;
    }

    public void record(List<ProcessExecution> processes) {
        logger.info("Calling repository to save a list of process execution");

        List<List<Process>> processExecutionToEvent = processes.stream()
                .map(this::buildProcessTriples)
                .collect(Collectors.toList());

        processRepository.save(processExecutionToEvent);
    }

    public void deleteProcessExecutionFrom(List<ProcessExecution> processes, String graph) {
        logger.info("Calling repository to delete a list of process execution");

        List<List<Process>> processExecutionToEvent = processes.stream()
                .map(this::buildProcessTriples)
                .collect(Collectors.toList());

        processRepository.deleteManyTriples(processExecutionToEvent, graph);
    }

    public void deleteGraph(String graph) {
        logger.info("Calling repository to delete a specific graph");

        processRepository.deleteGraph(graph);
    }

    private List<Process> buildProcessTriples(ProcessExecution processExecution) {
        BNode bnode = Values.bnode();

        return asList(
                buildTriple(bnode.toString(), "http://www.w3.org/2000/01/rdf-schema#Class", "http://purl.org/NET/c4dm/event.owl#Event"),
                buildTriple(bnode.toString(), "http://purl.org/NET/c4dm/event.owl#agent", processExecution.getResponsible()),
                buildTriple(bnode.toString(), "http://purl.org/NET/c4dm/event.owl#product", processExecution.getProduct()),
                buildTriple(bnode.toString(), "http://purl.org/NET/c4dm/event.owl#factor", processExecution.getFactor()),
                buildTriple(bnode.toString(), "http://purl.org/NET/c4dm/event.owl#time", processExecution.getTime()),
                buildTriple(bnode.toString(), "http://purl.org/NET/c4dm/event.owl#place", processExecution.getPlace())
        );
    }

    private Process buildTriple(String resource, String property, String value) {
        return new Process(resource, property, value);
    }
}
