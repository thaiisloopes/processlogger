package com.semanticweb.processlogger.domain.applications;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.domain.resources.Triple;
import com.semanticweb.processlogger.domain.resources.User;
import com.semanticweb.processlogger.inbound.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.infrastructure.repository.TripleRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserApplication {
    private static final Logger logger = getLogger(UserApplication.class);

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(User user) throws URISyntaxException {
        logger.info("Calling repository to save an user");

        List<Triple> userToTriples = buildTriples(user);

        repository.save(userToTriples);

        return buildResourceCreationResponse(userToTriples);
    }

    private List<Triple> buildTriples(User user) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/users/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, "http://www.w3.org/2000/01/rdf-schema#Class", "http://purl.org/saeg/ontologies/bpeo#User"),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#name", user.getName()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#password", user.getPassword())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
