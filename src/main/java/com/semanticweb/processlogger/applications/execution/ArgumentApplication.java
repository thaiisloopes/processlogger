package com.semanticweb.processlogger.applications.execution;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.execution.resources.Argument;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import com.semanticweb.processlogger.repositories.resources.Triple;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ArgumentApplication {
    private static final Logger logger = getLogger(ArgumentApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Argument argument) throws URISyntaxException {
        logger.info("Calling repository to save an argument");

        List<Triple> argumentToTriples = buildTriples(argument);

        repository.save(argumentToTriples);

        return buildResourceCreationResponse(argumentToTriples);
    }

    private List<Triple> buildTriples(Argument argument) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/arguments/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/saeg/ontologies/bpeo#Argument"),
                buildTriple(resourceUri, RDF_TYPE_URI, argument.getResourceType()),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, argument.getName()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#value", argument.getValue())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
