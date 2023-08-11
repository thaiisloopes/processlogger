package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.repositories.resources.Triple;
import com.semanticweb.processlogger.applications.metamodel.resources.Process;
import com.semanticweb.processlogger.repositories.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class ProcessApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProcessApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_PROCESS_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Process";
    private static final String BBO_FLOW_ELEMENTS_CONTAINER_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowElementsContainer";
    private static final String BBO_HAS_FLOW_ELEMENTS_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_flowElements";

    @Autowired
    private TripleRepository repository;

    public Model getProcess() {
        logger.info("Calling repository to get all processes");

        //TODO: precisa adaptar essa query pra pegar todas as URIs dos processos
        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = repository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public ResourceCreationResponse save(Process process) throws URISyntaxException {
        logger.info("Calling repository to save a process");

        List<Triple> processToTriples = buildProcessTriples(process);

        repository.save(processToTriples);

        return buildResourceCreationResponse(processToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildProcessTriples(Process process) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/processes/" + UlidCreator.getUlid();

        List<Triple> triples = asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_PROCESS_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_ELEMENTS_CONTAINER_CLASS_URI)
        );

        triples.addAll(
                process.getFlowElements().stream().map(flowElement ->
                    buildTriple(resourceUri, BBO_HAS_FLOW_ELEMENTS_PROPERTY_URI, flowElement)
                ).collect(toList())
        );

        return triples;
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
