package com.semanticweb.processlogger.applications.execution;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.execution.resources.Flow;
import com.semanticweb.processlogger.repositories.resources.Triple;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class FlowApplication {
    private static final Logger logger = getLogger(FlowApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Flow flow) throws URISyntaxException {
        logger.info("Calling repository to save a flow");

        List<Triple> flowToTriples = buildTriples(flow);

        repository.save(flowToTriples);

        return buildResourceCreationResponse(flowToTriples);
    }

    private List<Triple> buildTriples(Flow flow) {
        String resourceUri = "http://www.example.com/conference/flows/" + UlidCreator.getUlid();

        List<Triple> triples = new ArrayList<>(asList(
                buildTriple(resourceUri, RDF_TYPE_URI, "http://purl.org/saeg/ontologies/bpeo#Flow"),
                buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#flowStatus", flow.getFlowStatus().toString()),
                buildTriple(flow.getBelongsTo(), "http://purl.org/saeg/ontologies/bpeo#flow", resourceUri)
        ));

        if(flow.getInitialFlow()) {
            triples.add(
                    buildTriple(flow.getBelongsTo(), "http://purl.org/saeg/ontologies/bpeo#initialFlow", resourceUri)
            );
        }

        if(flow.getFrom() != null)
            triples.add(buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#source", valueOf(flow.getFrom())));
        if(flow.getTo() != null)
            triples.add(buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#target", valueOf(flow.getTo())));

        if(flow.getSteps() != null) {
            triples.addAll(
                    flow.getSteps().stream().map(step ->
                            buildTriple(resourceUri, "http://purl.org/saeg/ontologies/bpeo#step", step)
                    ).collect(toList())
            );
        }

        return triples;
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
