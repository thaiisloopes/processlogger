package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.metamodel.resources.Resource;
import com.semanticweb.processlogger.applications.metamodel.resources.SubProcess;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.TripleRepository;
import com.semanticweb.processlogger.repositories.resources.Triple;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SubProcessApplication {
    private static final Logger logger = getLogger(SubProcessApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_TASK_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Task";
    private static final String BBO_ACTIVITY_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Activity";
    private static final String BBO_FLOW_NODE_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowNode";
    private static final String BBO_FLOW_ELEMENT_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowElement";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";
    private static final String BBO_HAS_IO_SPECIFICATION_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_ioSpecification";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(SubProcess subProcess, String processId) throws URISyntaxException {
        logger.info("Calling repository to save a subProcess");

        List<Triple> subProcessToTriples = buildTriples(subProcess, processId);

        repository.save(subProcessToTriples);

        return buildResourceCreationResponse(subProcessToTriples);
    }
    private List<Triple> buildTriples(SubProcess subProcess, String processId) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/processes/" + processId + "/subprocesses" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_TASK_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_ACTIVITY_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_NODE_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_ELEMENT_CLASS_URI),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, subProcess.getName()),
                buildTriple(resourceUri, BBO_HAS_IO_SPECIFICATION_PROPERTY_URI, subProcess.getInputOutputSpecification())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
