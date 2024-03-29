package com.semanticweb.processlogger.repositories;

import com.semanticweb.processlogger.repositories.resources.Triple;
import com.stardog.ext.spring.RowMapper;
import com.stardog.ext.spring.SnarlTemplate;
import com.stardog.stark.query.BindingSet;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class TripleRepository {

    static final Logger logger = getLogger(TripleRepository.class);

    @Autowired
    public SnarlTemplate snarlTemplate;

    public List<Triple> get(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get all triples.");

        return snarlTemplate.query(queryString, new RowMapper<Triple>() {
            @Override
            public Triple mapRow(BindingSet bindingSet) {
                return new Triple(
                        requireNonNull(bindingSet.get("s")).toString(),
                        requireNonNull(bindingSet.get("p")).toString(),
                        requireNonNull(bindingSet.get("o")).toString()
                );
            }
        });
    }

    public List<Triple> getArgumentById(String argumentId) {
        logger.info("Calling StarDog from SnarlTemplate to get argument by id");
        String resourceUri = "http://www.example.com/conference/arguments/" + argumentId;
        String queryString = "SELECT ?p ?o WHERE { <" + resourceUri + "> ?p ?o. }";

        return snarlTemplate.query(queryString, new RowMapper<Triple>() {
            @Override
            public Triple mapRow(BindingSet bindingSet) {
                return new Triple(
                        resourceUri,
                        requireNonNull(bindingSet.get("p")).toString(),
                        requireNonNull(bindingSet.get("o")).toString()
                );
            }
        });
    }

    public List<Triple> getProcessExecutionById(String processId, String processExecutionId) {
        logger.info("Calling StarDog from SnarlTemplate to get process execution by id");
        String resourceUri = "http://www.example.com/conference/processes/" + processId + "/executions/" + processExecutionId;
        String queryString = "SELECT ?p ?o WHERE { <" + resourceUri + "> ?p ?o. }";

        return snarlTemplate.query(queryString, new RowMapper<Triple>() {
            @Override
            public Triple mapRow(BindingSet bindingSet) {
                return new Triple(
                        resourceUri,
                        requireNonNull(bindingSet.get("p")).toString(),
                        requireNonNull(bindingSet.get("o")).toString()
                );
            }
        });
    }

    public List<Triple> getProcessById(String processId) {
        logger.info("Calling StarDog from SnarlTemplate to get process by id");
        String resourceUri = "http://purl.org/saeg/ontologies/bpeo/processes/" + processId;
        String queryString = "SELECT ?p ?o WHERE { <" + resourceUri + "> ?p ?o. }";

        return snarlTemplate.query(queryString, new RowMapper<Triple>() {
            @Override
            public Triple mapRow(BindingSet bindingSet) {
                return new Triple(
                        resourceUri,
                        requireNonNull(bindingSet.get("p")).toString(),
                        requireNonNull(bindingSet.get("o")).toString()
                );
            }
        });
    }

    public List<Triple> getProcesses() {
        logger.info("Calling StarDog from SnarlTemplate to get all processes");
        String queryString = "SELECT ?s WHERE { ?s a <https://www.irit.fr/recherches/MELODI/ontologies/BBO#Process> }";

        return snarlTemplate.query(queryString, new RowMapper<Triple>() {
            @Override

            public Triple mapRow(BindingSet bindingSet) {
                return new Triple(
                        requireNonNull(bindingSet.get("s")).toString(),
                        "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
                        "https://www.irit.fr/recherches/MELODI/ontologies/BBO#Process"
                );
            }
        });
    }

    public List<List<Triple>> getActivities(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get activities");

        List<String> activities = snarlTemplate.query(queryString, bindingSet ->
                requireNonNull(bindingSet.get("activity")).toString()
        );

        return activities.stream().map(
                activity -> snarlTemplate.query(
                        "select ?p ?o where { <" + activity + "> ?p ?o. }",
                        bindingSet -> new Triple(
                                activity,
                                requireNonNull(bindingSet.get("p")).toString(),
                                requireNonNull(bindingSet.get("o")).toString()
                        ))
        ).collect(toList());
    }

    public List<List<Triple>> getProcessesExecutions(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get processes executions for given taskId");

        List<String> executions = snarlTemplate.query(queryString, bindingSet ->
                requireNonNull(bindingSet.get("execution")).toString()
        );

        return executions.stream().map(
                execution -> snarlTemplate.query(
                        "select ?p ?o where { <" + execution + "> ?p ?o. }",
                        bindingSet -> new Triple(
                                execution,
                                requireNonNull(bindingSet.get("p")).toString(),
                                requireNonNull(bindingSet.get("o")).toString()
                        ))
        ).collect(toList());
    }

    public List<List<Triple>> getObjects(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get objects for given taskId");

        List<String> objects = snarlTemplate.query(queryString, bindingSet ->
                requireNonNull(bindingSet.get("object")).toString()
        );

        return objects.stream().map(
                object -> snarlTemplate.query(
                        "select ?p ?o where { <" + object + "> ?p ?o. }",
                        bindingSet -> new Triple(
                                object,
                                requireNonNull(bindingSet.get("p")).toString(),
                                requireNonNull(bindingSet.get("o")).toString()
                        ))
        ).collect(toList());
    }

    public void save(List<Triple> triples) throws URISyntaxException {
        logger.info("Calling StarDog from SnarlTemplate to save triples");
        triples.forEach(
                triple -> {
                    String value = triple.getValue();

                    try {
                        if (value.contains("http")) {
                            snarlTemplate.add(
                                    new URI(triple.getResource()),
                                    new URI(triple.getProperty()),
                                    new URI(value)
                            );
                        } else {
                            snarlTemplate.add(
                                    triple.getResource(),
                                    triple.getProperty(),
                                    value
                            );
                        }

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void deleteManyTriples(List<List<Triple>> triples, String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete many triples");
        triples.forEach(
                triple -> triple.forEach(triple1 ->
                        snarlTemplate.remove(
                                triple1.getResource(),
                                triple1.getProperty(),
                                triple1.getValue(),
                                graph
                        )
                )
        );
    }

    public void deleteGraph(String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete all triples from a graph");

        snarlTemplate.remove(graph);
    }
}
