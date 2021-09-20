package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.domain.Process;
import com.semanticweb.processlogger.repository.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String PROCESS_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Process";
    private static final String THING_CLASS_URI = "https://schema.org/Thing";
    private static final String SCHEMA_NAME_PROPERTY_URI = "https://schema.org/name";
    private static final String SCHEMA_DESCRIPTION_PROPERTY_URI = "https://schema.org/description";
    private static final String BBO_HAS_PART_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_part";

    @Autowired
    private TripleRepository repository;

    public Model getProcess() {
        logger.info("Calling repository to get all recorded process");

        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

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

    public ResourceCreationResponse save(Process process) {
        logger.info("Calling repository to save a process");

        List<Triple> processToTriples = buildProcessTriples(process);
        List<Triple> hasPartToTriples = buildHasPartTriples(process, processToTriples.get(0).getResource());

        repository.save(processToTriples);
        repository.save(hasPartToTriples);

        return buildResourceCreationResponse(processToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildProcessTriples(Process process) {
        String resourceUri = "http://www.example.com/processes/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, PROCESS_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, THING_CLASS_URI),
                buildTriple(resourceUri, SCHEMA_NAME_PROPERTY_URI, process.getName()),
                buildTriple(resourceUri, SCHEMA_DESCRIPTION_PROPERTY_URI, process.getDescription())
        );
    }

    private List<Triple> buildHasPartTriples(Process process, String uri) {
        return process.getHasPart().stream()
                .map(part -> buildTriple(uri, BBO_HAS_PART_PROPERTY_URI, part))
                .collect(toList());
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
