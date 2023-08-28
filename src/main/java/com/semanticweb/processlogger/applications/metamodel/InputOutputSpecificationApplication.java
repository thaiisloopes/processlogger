package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.metamodel.resources.InputOutputSpecification;
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
public class InputOutputSpecificationApplication {
    private static final Logger logger = getLogger(ResourceApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_INPUT_OUTPUT_SPECIFICATION_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#InputOutputSpecification";
    private static final String BBO_HAS_RESOURCE_INPUTS_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_resourceInputs";
    private static final String BBO_HAS_RESOURCE_OUTPUTS_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_resourceOutputs";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(InputOutputSpecification inputOutputSpecification) throws URISyntaxException {
        logger.info("Calling repository to save an inputOutputSpecification");

        List<Triple> inputOutputSpecificationToTriples = buildTriples(inputOutputSpecification);

        repository.save(inputOutputSpecificationToTriples);

        return buildResourceCreationResponse(inputOutputSpecificationToTriples);
    }
    private List<Triple> buildTriples(InputOutputSpecification inputOutputSpecification) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/inputOutputSpecifications/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_INPUT_OUTPUT_SPECIFICATION_CLASS_URI),
                buildTriple(resourceUri, BBO_HAS_RESOURCE_INPUTS_PROPERTY_URI, inputOutputSpecification.getResourceInput()),
                buildTriple(resourceUri, BBO_HAS_RESOURCE_OUTPUTS_PROPERTY_URI, inputOutputSpecification.getResourceOutput())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
