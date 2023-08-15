package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.metamodel.resources.SequenceFlow;
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
public class SequenceFlowApplication {
    private static final Logger logger = getLogger(SequenceFlowApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_SEQUENCE_FLOW_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#SequenceFlow";
    private static final String BBO_FLOW_ELEMENT_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowElement";
    private static final String BBO_HAS_SOURCE_REF_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_sourceRef";
    private static final String BBO_HAS_TAERGET_REF_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_targetRef";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(SequenceFlow sequenceFlow) throws URISyntaxException {
        logger.info("Calling repository to save a sequenceFlow");

        List<Triple> sequenceFlowToTriples = buildTriples(sequenceFlow);

        repository.save(sequenceFlowToTriples);

        return buildResourceCreationResponse(sequenceFlowToTriples);
    }

    private List<Triple> buildTriples(SequenceFlow sequenceFlow) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/sequenceflows/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_SEQUENCE_FLOW_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_ELEMENT_CLASS_URI),
                buildTriple(resourceUri, BBO_HAS_SOURCE_REF_PROPERTY_URI,sequenceFlow.getSourceRef()),
                buildTriple(resourceUri, BBO_HAS_TAERGET_REF_PROPERTY_URI,sequenceFlow.getTargetRef())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
