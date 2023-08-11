package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.metamodel.resources.Role;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import com.semanticweb.processlogger.repositories.resources.Triple;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RoleApplication {
    private static final Logger logger = getLogger(RoleApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_ROLE_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Role";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";
    private static final String BBO_IS_RESPONSIBLE_FOR_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#is_responsibleFor";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Role role) throws URISyntaxException {
        logger.info("Calling repository to save a role");

        List<Triple> roleToTriples = buildTriples(role);

        repository.save(roleToTriples);

        return buildResourceCreationResponse(roleToTriples);
    }

    private List<Triple> buildTriples(Role role) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/resources/" + UlidCreator.getUlid();

        List<Triple> triples = asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_ROLE_CLASS_URI),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, role.getName())
        );

        triples.addAll(
                role.getRelatedActivities().stream().map(activity ->
                        buildTriple(resourceUri, BBO_IS_RESPONSIBLE_FOR_PROPERTY_URI, activity)
                ).collect(toList())
        );

        return triples;
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
