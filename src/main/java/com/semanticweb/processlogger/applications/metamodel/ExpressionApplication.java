package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.applications.metamodel.resources.Expression;
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
public class ExpressionApplication {
    private static final Logger logger = getLogger(ExpressionApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_EXPRESSION_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Expression";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";

    @Autowired
    private TripleRepository repository;

    public ResourceCreationResponse save(Expression expression) throws URISyntaxException {
        logger.info("Calling repository to save an expression");

        List<Triple> expressionToTriples = buildTriples(expression);

        repository.save(expressionToTriples);

        return buildResourceCreationResponse(expressionToTriples);
    }

    private List<Triple> buildTriples(Expression expression) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/expressions/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_EXPRESSION_CLASS_URI),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, expression.getName())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }
}
