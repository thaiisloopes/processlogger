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
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RoleApplication {
    private static final Logger logger = getLogger(RoleApplication.class);

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Role role) throws URISyntaxException {
        logger.info("Calling repository to save a role");

        List<Triple> roleToTriples = buildTriples(role);

        repository.save(roleToTriples);

        return buildResourceCreationResponse(roleToTriples);
    }
    private List<Triple> buildTriples(Role role) {
        //TODO: validar como serao registrados as instancias dos modelos
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/resources/" + UlidCreator.getUlid();

        return Collections.emptyList();
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
