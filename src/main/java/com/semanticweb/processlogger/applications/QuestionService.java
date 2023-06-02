package com.semanticweb.processlogger.applications;

import com.semanticweb.processlogger.repositories.resources.Triple;
import com.semanticweb.processlogger.repositories.TripleRepository;
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

        List<List<Triple>> activitiesTriples = repository.getActivities(queryString);

        return buildModelFrom(activitiesTriples);
    }

    public Model getProcessesExecutionsWithTask(String taskId) {
        logger.info("Calling repository to get all processes executions for given taskId");

        String queryString = "PREFIX event: <http://purl.org/saeg/schemas/event#> SELECT ?execution WHERE { ?execution event:transitive_sub_event ?taskExecution. ?taskExecution a <http://www.example.com/tasks/" + taskId + ">.}";

        List<List<Triple>> processesExecutionsTriples = repository.getProcessesExecutions(queryString);

        return buildModelFrom(processesExecutionsTriples);
    }

    public Model getObjectsFromTask(String taskId) {
        logger.info("Calling repository to get all objects for given taskId");

        String queryString = "PREFIX event: <http://purl.org/NET/c4dm/event.owl#> PREFIX event2: <http://purl.org/saeg/schemas/event#> SELECT ?object WHERE { ?execution event2:transitive_sub_event ?taskExecution. ?taskExecution a <http://www.example.com/tasks/" + taskId + ">. ?taskExecution event:factor ?object. }";

        List<List<Triple>> objectsTriples = repository.getObjects(queryString);

        return buildModelFrom(objectsTriples);
    }

    @NotNull
    private Model buildModelFrom(List<List<Triple>> triples) {
        Model model = createDefaultModel();
        triples.forEach(
                triples1 -> triples1.forEach(
                        triple -> {
                            Resource resource = model.createResource(triple.getResource());
                            Property property = model.createProperty(triple.getProperty());
                            model.add(resource, property, triple.getValue());
                        }
                )
        );
        return model;
    }
}
