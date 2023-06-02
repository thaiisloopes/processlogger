package com.semanticweb.processlogger.applications;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.resources.Flow;
import com.semanticweb.processlogger.repositories.resources.Triple;
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
public class FlowApplication {
    private static final Logger logger = getLogger(FlowApplication.class);

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Flow flow) throws URISyntaxException {
        logger.info("Calling repository to save a flow");

        List<Triple> flowToTriples = buildTriples(flow);

        repository.save(flowToTriples);

        return buildResourceCreationResponse(flowToTriples);
    }

    private List<Triple> buildTriples(Flow flow) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/flows/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, "http://www.w3.org/2000/01/rdf-schema#Class", "http://purl.org/saeg/ontologies/bpeo#Flow"),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#status", flow.getFlowStatus().toString()),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#currentNode", flow.getCurrentNode().toString())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
