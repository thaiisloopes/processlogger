package com.semanticweb.processlogger.service;

import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.repository.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.jena.rdf.model.ModelFactory.createDefaultModel;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class QuestionService {

    private static final Logger logger = getLogger(QuestionService.class);

    @Autowired
    private TripleRepository repository;

    public Model getActivities(String processId, String processExecutionId) {
        logger.info("Calling repository to get all activities of a process execution");

        String queryString = "PREFIX event: <http://purl.org/saeg/schemas/event#> " +
                "SELECT ?activity " +
                "WHERE { " +
                "<http://www.example.com/processes/" + processId + "/executions/" + processExecutionId + "> " +
                "event:transitive_sub_event ?activity. }";

        return buildModelFrom(queryString);
    }

    public Model getProcessesExecutionsWithTask(String taskId) {
        logger.info("Calling repository to get all processes executions for given taskId");

        String queryString = "PREFIX event: <http://purl.org/saeg/schemas/event#>" +
                "SELECT ?execution" +
                "WHERE {" +
                "    ?execution event:transitive_sub_event ?taskExecution." +
                "    ?taskExecution a <http://www.example.com/tasks/" + taskId + ">." +
                "}";

        return buildModelFrom(queryString);
    }

    public Model getObjectsFromTask(String taskId) {
        logger.info("Calling repository to get all objects for given taskId");

        String queryString = "PREFIX event: <http://purl.org/saeg/schemas/event#>" +
                "SELECT ?object" +
                "WHERE {" +
                "    ?execution event:transitive_sub_event ?taskExecution." +
                "    ?taskExecution a <http://www.example.com/tasks/" + taskId + ">." +
                "    ?taskExecution event:factor ?object." +
                "}";

        return buildModelFrom(queryString);
    }

    @NotNull
    private Model buildModelFrom(String queryString) {
        Model model = createDefaultModel();
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
}
