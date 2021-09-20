package com.semanticweb.processlogger.repository;

import com.semanticweb.processlogger.domain.Triple;
import com.stardog.ext.spring.SnarlTemplate;
import com.stardog.stark.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import static java.util.Objects.*;

@Repository
public class ProcessExecutionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionRepository.class);

    @Autowired
    SnarlTemplate snarlTemplate;

    public List<Triple> get(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get all triples");
        return snarlTemplate.query(queryString, bindingSet -> new Triple(
                requireNonNull(bindingSet.get("s")).toString(),
                requireNonNull(bindingSet.get("p")).toString(),
                requireNonNull(bindingSet.get("o")).toString()
        ));
    }

    public void save(List<Triple> triples) {
        logger.info("Calling StarDog from SnarlTemplate to save a process triple");
        triples.forEach(
                triple ->
                        snarlTemplate.add(
                                triple.getResource(),
                                triple.getProperty(),
                                triple.getValue()
                        )
        );
    }

    public void deleteManyTriples(List<List<Triple>> triples, String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete many process triples");
        triples.forEach(
                triple -> triple.forEach(triple1 ->
                        snarlTemplate.remove(
                                triple1.getResource(),
                                triple1.getProperty(),
                                triple1.getValue(),
                                Values.DEFAULT_GRAPH.localName()
                        )
                )
        );
    }

    public void deleteGraph(String graph) {
        logger.info("Calling StarDog grom SnarlTemplate to delete all process triples from a graph");

        snarlTemplate.remove(graph);
    }
}
