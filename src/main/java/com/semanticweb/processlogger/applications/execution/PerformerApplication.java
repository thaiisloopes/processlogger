package com.semanticweb.processlogger.applications.execution;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.repositories.resources.Triple;
import com.semanticweb.processlogger.applications.execution.resources.Performer;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PerformerApplication {
    private static final Logger logger = getLogger(PerformerApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_ROLE_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Role";
    private static final String BBO_PLAYS_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#plays";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";
    private static final String FOAF_NAME_PROPERTY_URI = "http://xmlns.com/foaf/0.1/#name";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Performer performer) throws URISyntaxException {
        logger.info("Calling repository to save a performer");

        List<Triple> performerToTriples = buildTriples(performer);

        repository.save(performerToTriples);

        return buildResourceCreationResponse(performerToTriples);
    }

    private List<Triple> buildTriples(Performer performer) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/performers/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/saeg/ontologies/bpeo#Performer"),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_ROLE_CLASS_URI),
                buildTriple(resourceUri, BBO_PLAYS_PROPERTY_URI, performer.getRole()),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, performer.getName()),
                buildTriple(resourceUri, FOAF_NAME_PROPERTY_URI, performer.getName())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
