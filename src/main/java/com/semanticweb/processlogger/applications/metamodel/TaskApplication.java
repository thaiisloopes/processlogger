package com.semanticweb.processlogger.applications.metamodel;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.applications.metamodel.resources.Task;
import com.semanticweb.processlogger.repositories.resources.Triple;
import com.semanticweb.processlogger.repositories.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TaskApplication {

    private static final Logger logger = getLogger(TaskApplication.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String BBO_TASK_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Task";
    private static final String BBO_ACTIVITY_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Activity";
    private static final String BBO_FLOW_NODE_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowNode";
    private static final String BBO_FLOW_ELEMENT_CLASS_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#FlowElement";
    private static final String BBO_NAME_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#name";
    private static final String BBO_HAS_IO_SPECIFICATION_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_ioSpecification";
    private static final String BBO_IS_RESPONSIBLE_FOR_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#is_responsibleFor";
    private static final String BBO_HAS_FLOW_ELEMENTS_PROPERTY_URI = "https://www.irit.fr/recherches/MELODI/ontologies/BBO#has_flowElements";


    @Autowired
    private TripleRepository repository;

    public Model getTasks() {
        logger.info("Calling repository to get all tasks");

        //TODO: precisa adaptar essa query pra pegar todas as URIs das tarefas
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

    public ResourceCreationResponse save(Task task, String processId) throws URISyntaxException {
        logger.info("Calling repository to save a task");

        List<Triple> taskToTriples = buildTaskTriples(task, processId);

        repository.save(taskToTriples);

        return buildResourceCreationResponse(taskToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildTaskTriples(Task task, String processId) {
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/processes/" + processId + "/tasks/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_TASK_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_ACTIVITY_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_NODE_CLASS_URI),
                buildTriple(resourceUri, RDF_TYPE_URI, BBO_FLOW_ELEMENT_CLASS_URI),
                buildTriple(resourceUri, BBO_NAME_PROPERTY_URI, task.getName()),
                buildTriple(resourceUri, BBO_HAS_IO_SPECIFICATION_PROPERTY_URI, task.getInputOutputSpecification()),
                buildTriple(task.getRole(), BBO_IS_RESPONSIBLE_FOR_PROPERTY_URI, resourceUri),
                buildTriple("http://purl.org/saeg/ontologies/bpeo/processes/" + processId, BBO_HAS_FLOW_ELEMENTS_PROPERTY_URI, resourceUri)
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
